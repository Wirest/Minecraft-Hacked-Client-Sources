package store.shadowclient.client.hud.draggablehud;

public interface IRenderConfig {
	public void save(ScreenPosition pos);

	public ScreenPosition load();
}
