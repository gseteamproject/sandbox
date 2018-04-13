package mapRunner.map.navigation;

import jade.content.Predicate;

public class NavigationToTarget implements Predicate {
	private static final long serialVersionUID = 2914983717229055059L;

	private Target target = new Target();

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	private Navigation navigation = new Navigation();

	public Navigation getNavigation() {
		return navigation;
	}

	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}
}
