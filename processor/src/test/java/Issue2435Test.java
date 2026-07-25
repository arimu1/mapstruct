/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */

import org.junit.jupiter.api.extension.RegisterExtension;
import org.mapstruct.ap.testutil.IssueKey;
import org.mapstruct.ap.testutil.ProcessorTest;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.runner.GeneratedSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Nested types in the unnamed package must compile without invalid imports
 * such as {@code import Outer.Nested}.
 *
 * Lives in the unnamed package so {@link WithClasses} can reference the mapper sources.
 */
@IssueKey("2435")
@WithClasses(UnnamedPackageNestedMapperIssue.class)
public class Issue2435Test {

    @RegisterExtension
    final GeneratedSource generatedSource = new GeneratedSource();

    @ProcessorTest
    public void shouldCompileAndMapNestedTypesInUnnamedPackage() {
        UnnamedPackageNestedMapperIssue.Source source = new UnnamedPackageNestedMapperIssue.Source();
        source.setProperty( "value" );

        UnnamedPackageNestedMapperIssue.Target target =
            UnnamedPackageNestedMapperIssue.NestedUnnamedPackageMapper.INSTANCE.map( source );

        assertThat( target ).isNotNull();
        assertThat( target.getProperty() ).isEqualTo( "value" );

        // Types from the unnamed package must not be imported (JLS §7.5).
        generatedSource.forMapper( UnnamedPackageNestedMapperIssue.NestedUnnamedPackageMapper.class )
            .content()
            .doesNotContain( "import UnnamedPackageNestedMapperIssue" );
    }
}
