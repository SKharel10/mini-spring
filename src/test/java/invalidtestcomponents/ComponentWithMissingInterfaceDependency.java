package invalidtestcomponents;

import com.project.minispring.Component;

@Component
public class ComponentWithMissingInterfaceDependency {
    private InterfaceDependency dependency;

    public ComponentWithMissingInterfaceDependency(InterfaceDependency dependency) {
        this.dependency = dependency;
    }
}
