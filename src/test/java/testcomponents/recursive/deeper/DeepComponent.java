package testcomponents.recursive.deeper;

import com.project.minispring.Component;

@Component
public class DeepComponent {
    private DeepComponentDependency dependency;

    public DeepComponent(DeepComponentDependency dependency) {
        this.dependency = dependency;
    }

    public DeepComponentDependency getDependency() {
        return dependency;
    }
}
