package audiabolikal.itemBuilding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A class for choosing a colour from a JColorChooser. Used when defining item
 * colours.
 * 
 * @author Samuel J. Sarjant
 */
public class ColourChooserPanel extends JPanel implements ChangeListener, ActionListener {
	/** The chosen colour. */
	private JDialog parent_;
	private Color chosenColour_ = Color.WHITE;
	private JColorChooser colourChooser_;
	
	public ColourChooserPanel(JDialog parent) {
		parent_ = parent;
		initialise();
	}
	
	private void initialise() {
		setLayout(new BorderLayout());
		
		colourChooser_ = new JColorChooser();
		colourChooser_.getSelectionModel().addChangeListener(this);
		add(colourChooser_, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(ItemBuilder.createButton("OK", this));
		buttonPanel.add(ItemBuilder.createButton("Cancel", this));
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public Color getChosenColour() {
		return chosenColour_;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		chosenColour_ = colourChooser_.getColor();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			parent_.setVisible(false);
		} else if (e.getActionCommand().equals("Cancel")) {
			chosenColour_ = null;
			parent_.setVisible(false);
		}
	}
}
