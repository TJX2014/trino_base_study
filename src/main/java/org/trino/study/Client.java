package org.trino.study;

public class Client {

    public static void main(String[] args) {
        io.trino.cli.Trino.main(args);

//        create table hive.clustering3.aa(a int, b varchar);
//        insert into hive.clustering3.aa values(11, 'aa');
//        select * from hive.clustering3.aa;
    }
}
