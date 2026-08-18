@Component
public class TestComponent {
    private NonComponentDependency dependency;

    public TestComponent(NonComponentDependency dependency) {
        this.dependency = dependency;
    }
}
