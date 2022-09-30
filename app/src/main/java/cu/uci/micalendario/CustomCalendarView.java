package cu.uci.micalendario;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.text.AttributedCharacterIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {

    ImageButton next, preview;

    TextView date;

    GridView gridView;

    DBOpenHelper dbOpenHelper;

    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.CANADA);
    Context context;

    GridAdapter gridAdapter;

    AlertDialog alertdialog;
    ArrayList<Date> dates = new ArrayList<>();
    ArrayList<Events> eventsList = new ArrayList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    public CustomCalendarView(Context context){
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attributeSet){
        super(context, attributeSet);
        this.context = context;
        InitializeLayout();
        SetUpCalendar();

        preview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout, null);
                EditText EventName = addView.findViewById(R.id.seteventid);
                TextView EventTime = addView.findViewById(R.id.eventtime);
                ImageButton SetTime = addView.findViewById(R.id.seteventtime);
                Button AddEvent = addView.findViewById(R.id.addevent);

                SetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_MiCalendario, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c =  Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformat = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                String event_Time = hformat.format(c.getTime());
                                EventTime.setText(event_Time);
                            }
                        }, hours, minutes, false);
                        timePickerDialog.show();
                    }
                });
                String date = dateFormat.format(dates.get(position));
                String month = monthFormat.format(dates.get(position));
                String year = yearFormat.format(dates.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year);
                        SetUpCalendar();
                        alertdialog.dismiss();
                    }
                });
                builder.setView(addView);
                alertdialog = builder.create();
                alertdialog.show();
            }
        });
    }


    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void SaveEvent(String event, String time, String date, String month, String year){
//        dbOpenHelper.SaveEvent(event,time,date,month,year,da);
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event,time,date,month,year,database);
        dbOpenHelper.close();

        Toast.makeText(context, "Evento Agregado", Toast.LENGTH_SHORT).show();
    }

    private void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        next = view.findViewById(R.id.fwbutton);
        preview = view.findViewById(R.id.backbutton);
        date = view.findViewById(R.id.currentDate);
        gridView = view.findViewById(R.id.calendarGrid);
    }

    private void SetUpCalendar(){
        String currentdate = dateFormat.format(calendar.getTime());
        date.setText(currentdate);
        dates.clear();
        Calendar monthcalendar = (Calendar) calendar.clone();
        monthcalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayofMonth = monthcalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthcalendar.add(Calendar.DAY_OF_MONTH, -firstDayofMonth);
//        CollectEventsMonthly(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));
//        CollectEventsMonthly("09", "2022");

        while(dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthcalendar.getTime());
            monthcalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridAdapter = new GridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(gridAdapter);
    }

    private void CollectEventsMonthly(String Month, String Year){
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsMonth("09", "2022", database);
        while (cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String monthz = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String yearz = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(date, event, monthz,time,yearz);
        }
        cursor.close();
        dbOpenHelper.close();
    }
}
