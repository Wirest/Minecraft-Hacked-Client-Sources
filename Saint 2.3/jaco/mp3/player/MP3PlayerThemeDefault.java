package jaco.mp3.player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

final class MP3PlayerThemeDefault implements MP3PlayerTheme {
   public void apply(final MP3Player player) {
      JButton playButton = new JButton();
      playButton.setText(">");
      playButton.setToolTipText("Play");
      playButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            player.play();
         }
      });
      JButton pauseButton = new JButton();
      pauseButton.setText("||");
      pauseButton.setToolTipText("Pause");
      pauseButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            player.pause();
         }
      });
      JButton stopButton = new JButton();
      stopButton.setText("#");
      stopButton.setToolTipText("Stop");
      stopButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            player.stop();
         }
      });
      JButton skipBackwardButton = new JButton();
      skipBackwardButton.setText("|<");
      skipBackwardButton.setToolTipText("Skip Backward");
      skipBackwardButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            player.skipBackward();
         }
      });
      JButton skipForwardButton = new JButton();
      skipForwardButton.setText(">|");
      skipForwardButton.setToolTipText("Skip Forward");
      skipForwardButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            player.skipForward();
         }
      });
      final JSlider volumeSlider = new JSlider();
      volumeSlider.setToolTipText("Volume");
      volumeSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            player.setVolume(volumeSlider.getValue());
         }
      });
      volumeSlider.setMinimum(0);
      volumeSlider.setMaximum(100);
      volumeSlider.setMajorTickSpacing(50);
      volumeSlider.setMinorTickSpacing(10);
      volumeSlider.setPaintTicks(true);
      volumeSlider.setPaintTrack(true);
      final JCheckBox repeatCheckBox = new JCheckBox();
      repeatCheckBox.setText("Repeat");
      repeatCheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            player.setRepeat(repeatCheckBox.isSelected());
         }
      });
      final JCheckBox shuffleCheckBox = new JCheckBox();
      shuffleCheckBox.setText("Shuffle");
      shuffleCheckBox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            player.setShuffle(shuffleCheckBox.isSelected());
         }
      });
      GroupLayout layout = new GroupLayout(player);
      player.setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(playButton).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(pauseButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(stopButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(skipBackwardButton).addPreferredGap(ComponentPlacement.RELATED).addComponent(skipForwardButton)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(shuffleCheckBox).addComponent(repeatCheckBox)).addPreferredGap(ComponentPlacement.RELATED).addComponent(volumeSlider, 0, 0, 32767))).addContainerGap(-1, 32767)));
      layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.TRAILING, false).addComponent(playButton, Alignment.LEADING, -1, -1, 32767).addGroup(Alignment.LEADING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(pauseButton).addComponent(stopButton).addComponent(skipBackwardButton).addComponent(skipForwardButton)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(shuffleCheckBox).addPreferredGap(ComponentPlacement.RELATED).addComponent(repeatCheckBox)).addComponent(volumeSlider, -2, 42, -2)))).addContainerGap(-1, 32767)));
   }
}
