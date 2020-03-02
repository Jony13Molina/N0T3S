package com.example.jonny.n0t3s.tabView;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ListView;

        import androidx.fragment.app.Fragment;

        import com.example.jonny.n0t3s.R;

public class Applications extends Fragment {
    public Applications() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.applications_xmlfragment, container, false);
    }
}