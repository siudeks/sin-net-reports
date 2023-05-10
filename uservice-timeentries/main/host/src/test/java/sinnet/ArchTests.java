package sinnet;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.CompositeArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.GeneralCodingRules;

import jakarta.persistence.Entity;

/** Architectural tests. */
@AnalyzeClasses(packages = "sinnet", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchTests {

  @ArchTest
  static final ArchRule noDependencyOnUpperPackage = noClasses().should(new DependOnUpperPackagesCondition())
      .because("that might prevent packages on that level from being split into separate artifacts in a clean way");

  // Classes should not access System.in , System.out or System.err
  // Classes should not use java util logging
  // Classes should not use joda time
  // Methods should not throw generic exception
  // For spring classes, there should be any field injection (@Autowired), use constrctor injection
  @ArchTest
  static final ArchRule implement_general_coding_practices = CompositeArchRule.of(GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS)
    .and(GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS)
    .and(GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING)
    .and(GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME)
    .and(GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION)
    .because("These are Voilation of general coding rules");


  // @ArchTest
  // static final ArchRule noPublicControllers = classes().that()
  //     .areAnnotatedWith(RestController.class)
  //     .should()
  //     .bePackagePrivate()
  //     .andShould()
  //     .resideInAPackage("sinnet.web");

  @ArchTest
  static final ArchRule dboNameAndVisibility = classes().that()
      .areAnnotatedWith(Entity.class)
      .should()
      .bePackagePrivate()
      .andShould()
      .haveSimpleNameEndingWith("Dbo");
    
  }

// Copy of DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES from ArchUnit
// suite, but ignoring dependency on clases named '...PackageMarker'
class DependOnUpperPackagesCondition extends ArchCondition<JavaClass> {

  DependOnUpperPackagesCondition() {
    super("depend on upper packages");
  }

  @Override
  public void check(final JavaClass clazz, final ConditionEvents events) {
    for (var dependency : clazz.getDirectDependenciesFromSelf()) {
      var dependencyOnUpperPackage = isDependencyOnUpperPackage(dependency.getOriginClass(),
          dependency.getTargetClass());
      events.add(new SimpleConditionEvent(dependency, dependencyOnUpperPackage, dependency.getDescription()));
    }
  }

  private boolean isDependencyOnUpperPackage(JavaClass origin, JavaClass target) {
    if (isPackageMarker(target)) {
      return false;
    }
    if (isPortAdapter(target)) {
      return false;
    }
    String originPackageName = origin.getPackageName();
    String targetSubPackagePrefix = target.getPackageName() + ".";
    return originPackageName.startsWith(targetSubPackagePrefix);
  }

  private boolean isPackageMarker(JavaClass target) {
    return target.getSimpleName().endsWith("PackageMarker");
  }

  private boolean isPortAdapter(JavaClass target) {
    return target.getSimpleName().endsWith("Facade");
  }

}
