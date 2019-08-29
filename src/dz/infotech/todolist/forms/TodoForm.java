package dz.infotech.todolist.forms;

import com.codename1.components.FloatingActionButton;
import com.codename1.components.ToastBar;
import com.codename1.io.Log;
import com.codename1.ui.CN;
import com.codename1.ui.Component;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import dz.infotech.todolist.utils.AppConfig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.codename1.ui.CN.*;
import static dz.infotech.todolist.utils.AppConfig.DEFAULT_STORAGE_FILE_NAME;
import static dz.infotech.todolist.utils.AppConfig.TODO_APP_TITLE;

public class TodoForm extends Form {

    // Event handler to save typed data automatically
    private ActionListener saver;

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

        load();
    }

    private ActionListener getAutoSave(){
        if(saver == null){
            // saver is an action listener that invoke save
            saver = (e) -> save();
        }
        return saver;
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

    private void load(){
        // The first file we run the input file doesn't exist
        if(existsInStorage(DEFAULT_STORAGE_FILE_NAME)){
            try(DataInputStream dis = new DataInputStream(createStorageInputStream(DEFAULT_STORAGE_FILE_NAME));
            ){
                int size = dis.readInt();
                for(int iter = 0; iter < size; iter++){
                    boolean checked = dis.readBoolean();
                    TodoItem i = new TodoItem(dis.readUTF(), checked,  getAutoSave());
                    add(i);
                }
            }catch(IOException err){
                Log.e(err);
                ToastBar.showErrorMessage("Error loading items.");
            }
        }
    }

    private void save(){
        // opens a storage file for writ the given name
        try(DataOutputStream dos = new DataOutputStream(createStorageOutputStream(DEFAULT_STORAGE_FILE_NAME));){

                dos.writeInt(getContentPane().getComponentCount());

                for(Component c: getContentPane()){
                    TodoItem i = (TodoItem) c;
                    dos.writeBoolean(i.isChecked());
                    // we write the value of every component in binary form
                    dos.writeUTF(i.getText());
                }
        }catch(IOException err){
            Log.e(err);
            ToastBar.showErrorMessage("Error saving items.");
        }
    }

    /**
     * Allow to add new Item in the todolist
     */
    private void addNewItem(){
        // create a new object instance for each item
        // ActionListener 'saver' is passed to the TodoItem
        // when it receives change notifications
        TodoItem td = new TodoItem("", false, getAutoSave());

        // We invoke Form's add method to add the item to the UI
        add(td);

        // lets the showing Form know we finished making changes
        revalidate();

        // Launch the device virtual keyboard so the user can start
        // typing text immediately
        td.edit();
    }
}
