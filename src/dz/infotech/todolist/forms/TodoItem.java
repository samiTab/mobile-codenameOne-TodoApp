package dz.infotech.todolist.forms;

import com.codename1.ui.CheckBox;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;

import static com.codename1.ui.CN.*;
import static dz.infotech.todolist.utils.AppConfig.*;


/**
 * Class responsible on managing the todolist's Items
 * Inheriting Container make it easy to detect if a TodoItem is checked
 */
public class TodoItem extends Container {

    private TextField nameText;

    // checkbox can be toggled between selected/unselected states
    private CheckBox done = new CheckBox();

    public TodoItem(String name, boolean checked, ActionListener onChange) {
        // with BorderLayout we can position a component
        super(new BorderLayout());

        // TextField accepts user text input with the virtual keyboard
        nameText = new TextField(name);

        // we want the text field to look like a label
        nameText.setUIID(ITEM_TEXTFIELD_UIID);

        // bind the action listener to the saver call
        nameText.addActionListener(onChange);
        done.addActionListener(onChange);

        // position the text field and the check box
        add(CENTER, nameText);
        add(EAST, done);

        // the check box is checked based on the boolean flag
        done.setSelected(checked);

        // define UI style for the Item
        underlineItem();

        // define UI style for the CheckBox
        done.setToggle(true);
        FontImage.setMaterialIcon(done, FontImage.MATERIAL_CHECK, 4);

    }

    // define the style of the item using the code
    private void underlineItem(){
        Style s = getAllStyles();
        s.setPaddingUnit(Style.UNIT_TYPE_PIXELS);
        s.setPadding(0,2,0,0);
        s.setBorder(Border.createLineBorder(2, 0xcccccc));
    }

    // launch the text editing on the device and opens the virtual keyboard
    public void edit(){
        nameText.startEditingAsync();
    }


    public boolean isChecked(){
        return done.isSelected();
    }

    public String getText(){
        return nameText.getText();
    }

}
