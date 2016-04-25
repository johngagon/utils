package jhg.sql.meta;

public class Column {
/*
Each column description has the following columns:

1. TABLE_CAT String => table catalog (may be null)
2. TABLE_SCHEM String => table schema (may be null)
3. TABLE_NAME String => table name
4. COLUMN_NAME String => column name
5. DATA_TYPE int => SQL type from java.sql.Types
6. TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
7. COLUMN_SIZE int => column size.
8. BUFFER_LENGTH is not used.
9. DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
10. NUM_PREC_RADIX int => Radix (typically either 10 or 2)
11. NULLABLE int => is NULL allowed.
		columnNoNulls - might not allow NULL values
		columnNullable - definitely allows NULL values
		columnNullableUnknown - nullability unknown
12. REMARKS String => comment describing column (may be null)
13. COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
14. SQL_DATA_TYPE int => unused
15. SQL_DATETIME_SUB int => unused
16. CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
17. ORDINAL_POSITION int => index of column in table (starting at 1)
18. IS_NULLABLE String => ISO rules are used to determine the nullability for a column.YES --- if the column can include NULLs NO --- if the column cannot include NULLs empty string --- if the nullability for the column is unknown
19. SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
20. SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
21. SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF)
22. SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
23. IS_AUTOINCREMENT String => Indicates whether this column is auto incremented YES --- if the column is auto incremented NO --- if the column is not auto incremented empty string --- if it cannot be determined whether the column is auto incremented
24. IS_GENERATEDCOLUMN String => Indicates whether this is a generated column YES --- if this a generated column NO --- if this not a generated column empty string --- if it cannot be determined whether this is a generated column

The COLUMN_SIZE column specifies the column size for the given column. For numeric data, this is the maximum precision. For character data, this is the length in characters. For datetime datatypes, this is the length in characters of the String representation (assuming the maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes. For the ROWID datatype, this is the length in bytes. Null is returned for data types where the column size is not applicable.
 */
}
