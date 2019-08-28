package dz.infotech.todolist.forms;

import com.codename1.components.FloatingActionButton;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

import static dz.infotech.todolist.utils.AppConfig.TODO_APP_TITLE;

public class TodoForm extends Form {

    /**
     * Constructor
     */
    public TodoForm(){
        // Create the default Form
        super(TODO_APP_TITLE, BoxLayout.y());

        // Add FAB
        FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);

        // We add items to containers but a FAB floats on top,
        // this method handles the floating aspect.
        // "this" means the current Form instance
        fab.bindFabToContainer(this);

        // when the FAB is clicked we invoke the addNewItem() method
        fab.addActionListener(e -> addNewItem());

        // add the clear command
        getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_CLEAR_ALL, e -> clearAll());
    }

    /**
     * removes all the checked Todo items
     */
    private void clearAll() {
        int cc = getContentPane().getComponentCount();

        // looping backwards, that means that if we remove a component the offset still won't change
        for(int i = cc-1; i>=0; i--){
            TodoItem t = (TodoItem)getContentPane().getComponentAt(i);
            // if the iTem is checked we remove it from its parent (The contentPane)
            if(t.isChecked()){
                t.remove();
            }
        }

        save();

        // this is a type of revalidate that animates
        getContentPane().animateLayout(300);
    }

    private void load(){};
    private void save(){};

    /**
     * Allow to add new Item in the todolist
     */
    private void addNewItem(){
        // create a new object instance for each item
        TodoItem td = new TodoItem("", false);

        // We invoke Form's add method to add the item to the UI
        add(td);

        // lets the showing Form know we finished making changes
        revalidate();

        // Launch the device virtual keyboard so the user can start
        // typing text immediately
        td.edit();
    }
}
