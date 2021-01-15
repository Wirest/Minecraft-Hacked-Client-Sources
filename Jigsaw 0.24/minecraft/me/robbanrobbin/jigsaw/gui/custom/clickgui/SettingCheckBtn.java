package me.robbanrobbin.jigsaw.gui.custom.clickgui;

public class SettingCheckBtn extends CheckBtn {
	
	private boolean value;
	private boolean preValue;
	private CheckBtnTask checkBtnTask;
	private CheckBtnSetting setting;
	
	public SettingCheckBtn(String title, boolean value, CheckBtnTask checkBtnTask, CheckBtnSetting setting) {
		this.setTitle(title);
		this.value = value;
		this.checkBtnTask = checkBtnTask;
		this.setting = setting;
		this.setToggled(value);
	}
	
	@Override
	public void update() {
		this.setToggled(setting.getValue());
		preValue = value;
		super.update();
	}
	
	@Override
	public void onClicked(double x, double y, int button) {
		super.onClicked(x, y, button);
		if(button == 0) {
			value = !setting.getValue();
			checkBtnTask.task(this);
			this.setToggled(value);
			System.out.println(setting.getValueField().getName());
		}
	}
	
	public boolean getValue() {
		return value;
	}
	
}
