package org.jd.benoggl

import javax.persistence.EntityManager

fun EntityManager.truncateAllTables() {
    createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()

    createNativeQuery(
        "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'"
    ).resultList
        .map { it as String }
        .forEach { tableName ->
            createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
        }

    createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
}