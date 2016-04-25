package jhg.sql.meta;

public class Type {
/*
TYPE_NAME String => Type name
DATA_TYPE int => SQL data type from java.sql.Types
PRECISION int => maximum precision
LITERAL_PREFIX String => prefix used to quote a literal (may be null)
LITERAL_SUFFIX String => suffix used to quote a literal (may be null)
CREATE_PARAMS String => parameters used in creating the type (may be null)
NULLABLE short => can you use NULL for this type.
typeNoNulls - does not allow NULL values
typeNullable - allows NULL values
typeNullableUnknown - nullability unknown
CASE_SENSITIVE boolean=> is it case sensitive.
SEARCHABLE short => can you use "WHERE" based on this type:
typePredNone - No support
typePredChar - Only supported with WHERE .. LIKE
typePredBasic - Supported except for WHERE .. LIKE
typeSearchable - Supported for all WHERE ..
UNSIGNED_ATTRIBUTE boolean => is it unsigned.
FIXED_PREC_SCALE boolean => can it be a money value.
AUTO_INCREMENT boolean => can it be used for an auto-increment value.
LOCAL_TYPE_NAME String => localized version of type name (may be null)
MINIMUM_SCALE short => minimum scale supported
MAXIMUM_SCALE short => maximum scale supported
SQL_DATA_TYPE int => unused
SQL_DATETIME_SUB int => unused
NUM_PREC_RADIX int => usually 2 or 10
 */
}
