//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.trino.server;

import com.google.common.collect.ImmutableList;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import org.joda.time.DateTime;

import java.io.PrintStream;
import java.lang.Runtime.Version;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryManagerMXBean;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Locale;
import java.util.OptionalLong;

final class TrinoSystemRequirements {
    private static final int MIN_FILE_DESCRIPTORS = 4096;
    private static final int RECOMMENDED_FILE_DESCRIPTORS = 8192;

    private TrinoSystemRequirements() {
    }

    public static void verifyJvmRequirements() {
        verifyJavaVersion();
        verify64BitJvm();
        verifyOsArchitecture();
        verifyByteOrder();
        verifyUsingG1Gc();
        verifyFileDescriptor();
        verifySlice();
    }

    private static void verify64BitJvm() {
        String dataModel = System.getProperty("sun.arch.data.model");
        if (!"64".equals(dataModel)) {
            failRequirement("Trino requires a 64-bit JVM (found %s)", dataModel);
        }

    }

    private static void verifyByteOrder() {
        ByteOrder order = ByteOrder.nativeOrder();
        if (!order.equals(ByteOrder.LITTLE_ENDIAN)) {
            failRequirement("Trino requires a little endian platform (found %s)", order);
        }

    }

    private static void verifyOsArchitecture() {

    }

    private static void verifyJavaVersion() {
        Runtime.Version required = Version.parse("17.0.3");
        if (Runtime.version().compareTo(required) < 0) {
            failRequirement("Trino requires Java %s at minimum (found %s)", required, Runtime.version());
        }

    }

    private static void verifyUsingG1Gc() {
        try {
            List<String> garbageCollectors = (List)ManagementFactory.getGarbageCollectorMXBeans().stream().map(MemoryManagerMXBean::getName).collect(ImmutableList.toImmutableList());
            if (garbageCollectors.stream().noneMatch((name) -> {
                return name.toUpperCase(Locale.US).startsWith("G1 ");
            })) {
                warnRequirement("Current garbage collectors are %s. Trino recommends the G1 garbage collector.", garbageCollectors);
            }
        } catch (RuntimeException var1) {
            failRequirement("Cannot read garbage collector information: %s", var1);
        }

    }

    private static void verifyFileDescriptor() {
        OptionalLong maxFileDescriptorCount = getMaxFileDescriptorCount();
        if (maxFileDescriptorCount.isEmpty()) {
            failRequirement("Cannot read OS file descriptor limit");
        }

        if (maxFileDescriptorCount.getAsLong() < 4096L) {
            failRequirement("Trino requires at least %s file descriptors (found %s)", 4096, maxFileDescriptorCount.getAsLong());
        }

        if (maxFileDescriptorCount.getAsLong() < 8192L) {
            warnRequirement("Current OS file descriptor limit is %s. Trino recommends at least %s", maxFileDescriptorCount.getAsLong(), 8192);
        }

    }

    private static OptionalLong getMaxFileDescriptorCount() {
        return OptionalLong.of(10240);
    }

    private static void verifySlice() {
        Slice slice = Slices.wrappedBuffer(new byte[5]);
        slice.setByte(4, 222);
        slice.setByte(3, 173);
        slice.setByte(2, 190);
        slice.setByte(1, 239);
        if (slice.getInt(1) != -559038737) {
            failRequirement("Slice library produced an unexpected result");
        }

    }

    public static void verifySystemTimeIsReasonable() {
        int currentYear = DateTime.now().year().get();
        if (currentYear < 2022) {
            failRequirement("Trino requires the system time to be current (found year %s)", currentYear);
        }

    }

    private static void failRequirement(String format, Object... args) {
        PrintStream var10000 = System.err;
        String var10001 = String.format(format, args);
        var10000.println("ERROR: " + var10001);
        System.exit(100);
    }

    private static void warnRequirement(String format, Object... args) {
        PrintStream var10000 = System.err;
        String var10001 = String.format(format, args);
        var10000.println("WARNING: " + var10001);
    }
}
