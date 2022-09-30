package cu.uci.micalendario;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GridAdapter extends ArrayAdapter {
    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater inflater;

    public GridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events) {
        super(context, R.layout.single_cell_layout);

        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date monthDate = dates.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthDate);
        int Day = calendar.get(Calendar.DAY_OF_MONTH);
        int displayonth = calendar.get(Calendar.MONTH)+1;
        int displayYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        if(displayonth == currentMonth && displayYear == currentYear){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.purple_200));
        }else{
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }
        TextView DayNumber = view.findViewById(R.id.calendar_day);
        TextView EventNumber = view.findViewById(R.id.eventNumber);
        DayNumber.setText(String.valueOf(Day));
        Calendar eventCal = Calendar.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
//        for (int i = 0; i < events.size(); i++) {
//            eventCal.setTime(ConvertStringToDate(events.get(i).getDATE()));
//            if(Day == eventCal.get(Calendar.DAY_OF_MONTH) && displayonth == eventCal.get(Calendar.MONTH)+1 && displayYear == eventCal.get(Calendar.YEAR)){
//                arrayList.add(events.get(i).getEVENT());
//                EventNumber.setText(arrayList.size() + " Eventos");
//            }
//        }
        return view;
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddd", Locale.ENGLISH);
        Date date = null;
        try{
            date = format.parse(eventDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
