package invalidtestcomponents;
import com.project.minispring.Component;

@Component
public class ComponentWithNonComponentDependency {
    private final NonComponentDependency dependency;

    public ComponentWithNonComponentDependency(NonComponentDependency dependency) {
        this.dependency = dependency;
    }
}
