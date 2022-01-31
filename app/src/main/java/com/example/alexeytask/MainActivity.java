package com.example.alexeytask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button favorite;
    EditText from, to;
    ListView lv;
    ArrayList<String> favorites = new ArrayList<>();
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favorite = findViewById(R.id.btnFavorite);
        favorite.setOnClickListener(this);
        from = findViewById(R.id.etFrom);
        to = findViewById(R.id.etTo);
        lv = findViewById(R.id.listView);
        loadArray();                                                                    //загружаю сохраненнный список при запуске


        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, favorites);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {               //интерфейс слушающий нажатие на элемент списка
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedItem = favorites.get(position);                         //метод получает строку с позициец в списке выбранного элемента
                String[] separated = selectedItem.split(" ");                   //метод разбивает строку на массив до пробела и после
                from.setText(separated[0]);                                         //выводит первую часть массива "откуда"
                to.setText(separated[1]);                                           //выводит вторую часть массива "куда"
                return;

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {       //интерфейс слушающий длинное нажатие
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();  //метод, получает нажатый элемент в строку
                adapter.remove(selectedItem);                                          //удаляет выбранный элемент
                adapter.notifyDataSetChanged();                                         //сохраняет
                Toast.makeText(getApplicationContext(), selectedItem + " удалён.", Toast.LENGTH_SHORT).show(); //высплывающее уведомление
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFavorite:                                                          //по нажатию кнопки метод добавляет строку в список, а saveArray его сохраняет
                favorites.add(0, from.getText().toString() + " " + to.getText().toString());
                adapter.notifyDataSetChanged();
                saveArray();
                break;
            default:
                break;
        }

    }


    void saveArray() {                                                                      //метод сохраняющий список, он же указан в onClick по нажатию
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("Status_size", favorites.size());
        for (int i = 0; i < favorites.size(); i++) {
            //editor.remove("Status_"+i);
            editor.putString("Status_" + i, favorites.get(i));
        }
        editor.commit();
    }


    void loadArray() {                                                                  //метод загружающий список из сохраненного, он же указан в onCreate для отображения при запуске
        SharedPreferences shpr = PreferenceManager.getDefaultSharedPreferences(this);
        // favorites.clear();
        int size = shpr.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            favorites.add(shpr.getString("Status_" + i, null));
        }
    }


}