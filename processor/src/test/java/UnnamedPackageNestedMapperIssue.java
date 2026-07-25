/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Reproduction of issue #2435: nested types in the unnamed package must not produce
 * invalid imports such as {@code import UnnamedPackageNestedMapperIssue.Source}.
 */
public class UnnamedPackageNestedMapperIssue {

    @Mapper
    public interface NestedUnnamedPackageMapper {

        NestedUnnamedPackageMapper INSTANCE = Mappers.getMapper( NestedUnnamedPackageMapper.class );

        Target map(Source source);
    }

    public static class Source {

        private String property;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }

    public static class Target {

        private String property;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }
}
