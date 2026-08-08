/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.mapstruct.ap.test.bugs._4094;

import org.mapstruct.ap.testutil.IssueKey;
import org.mapstruct.ap.testutil.ProcessorTest;
import org.mapstruct.ap.testutil.WithClasses;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author arimu1
 */
@IssueKey("4094")
public class Issue4094Test {

    @ProcessorTest
    @WithClasses({ Issue4094Mapper.class, Issue4094PersonMapper.class })
    public void shouldDelegateToUsesMapperDespiteRedundantNestedIgnore() {
        Issue4094Mapper.Person person = new Issue4094Mapper.Person();
        person.setId( "p1" );
        person.setName( "Ada" );
        person.setGender( "F" );
        person.setRelatedInformation( "secret" );

        Issue4094Mapper.PersonInformation source = new Issue4094Mapper.PersonInformation();
        source.setType( "employee" );
        source.setRelatedPerson( person );

        Issue4094Mapper.PersonInformationDto dto = Issue4094Mapper.INSTANCE.toDto( source );

        assertThat( dto.getType() ).isEqualTo( "employee" );
        assertThat( dto.getRelatedPerson() ).isNotNull();
        assertThat( dto.getRelatedPerson().getId() ).isEqualTo( "p1" );
        assertThat( dto.getRelatedPerson().getName() ).isEqualTo( "Ada" );
        assertThat( dto.getRelatedPerson().getGenderCode() ).isEqualTo( "F" );
        // PersonMapper uses ignoreByDefault and does not map relatedInformation
        assertThat( dto.getRelatedPerson().getRelatedInformation() ).isNull();
    }

    @ProcessorTest
    @WithClasses({ Issue4094Mapper.class, Issue4094PersonMapper.class })
    public void baselineWithoutNestedIgnoreStillDelegatesToUsesMapper() {
        Issue4094Mapper.Person person = new Issue4094Mapper.Person();
        person.setId( "p2" );
        person.setName( "Bob" );
        person.setGender( "M" );

        Issue4094Mapper.PersonInformation source = new Issue4094Mapper.PersonInformation();
        source.setType( "contractor" );
        source.setRelatedPerson( person );

        Issue4094Mapper.PersonInformationDto dto =
            Issue4094Mapper.INSTANCE.toDtoWithoutNestedIgnore( source );

        assertThat( dto.getType() ).isEqualTo( "contractor" );
        assertThat( dto.getRelatedPerson() ).isNotNull();
        assertThat( dto.getRelatedPerson().getId() ).isEqualTo( "p2" );
        assertThat( dto.getRelatedPerson().getName() ).isEqualTo( "Bob" );
        assertThat( dto.getRelatedPerson().getGenderCode() ).isEqualTo( "M" );
    }

    @ProcessorTest
    @WithClasses(Issue4094WithoutUsesMapper.class)
    public void withoutUsesMapperStillMapsSameNamedPropertiesInsteadOfEmptyForgedMethod() {
        Issue4094WithoutUsesMapper.Person person = new Issue4094WithoutUsesMapper.Person();
        person.setId( "p3" );
        person.setName( "Cara" );
        person.setGender( "F" );
        person.setRelatedInformation( "should-be-ignored" );

        Issue4094WithoutUsesMapper.PersonInformation source =
            new Issue4094WithoutUsesMapper.PersonInformation();
        source.setType( "intern" );
        source.setRelatedPerson( person );

        Issue4094WithoutUsesMapper.PersonInformationDto dto =
            Issue4094WithoutUsesMapper.INSTANCE.toDto( source );

        assertThat( dto.getType() ).isEqualTo( "intern" );
        assertThat( dto.getRelatedPerson() ).isNotNull();
        assertThat( dto.getRelatedPerson().getId() ).isEqualTo( "p3" );
        assertThat( dto.getRelatedPerson().getName() ).isEqualTo( "Cara" );
        // no uses mapper: gender → genderCode is not name-based
        assertThat( dto.getRelatedPerson().getGenderCode() ).isNull();
        assertThat( dto.getRelatedPerson().getRelatedInformation() ).isNull();
    }
}
