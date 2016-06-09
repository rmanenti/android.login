package com.wizsyst.android.app.login.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.contracheque.PaycheckActivity;
import com.wizsyst.android.app.login.adapter.BeanAnoAdapter;
import com.wizsyst.android.app.login.adapter.BeanFolhaAdapter;
import com.wizsyst.android.app.login.adapter.BeanMesAdapter;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.sigem.mobile.sleo.beans.BeanAno;
import com.wizsyst.sigem.mobile.sleo.beans.BeanFolha;
import com.wizsyst.sigem.mobile.sleo.beans.BeanFolhas;
import com.wizsyst.sigem.mobile.sleo.beans.BeanMes;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ListSavedPaychecksFragment extends ListFragment {

    public static final String TAG = "ListSavedPaychecks";

    public static final int FRAGMENT_lIST_YEARS    = 0,
                            FRAGMENT_lIST_MONTHS   = 1,
                            FRAGMENT_lIST_PAYROLLS = 2;

    private OnFragmentInteractionListener listener;

    private TextView title;

    private BeanFolhas folhas;

    private List<BeanAno> years;
    private BeanAno year;

    private List<BeanMes> months;
    private BeanMes month;

    private List<BeanFolha> payrolls;
    private BeanFolha payroll;

    private StringBuilder data;

    private int fragmentType = FRAGMENT_lIST_YEARS;

    private File folderPaychecks,
                 folderYear,
                 folderMonth;

    public ListSavedPaychecksFragment() {}

    @Override
    public void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

        folhas = ( BeanFolhas ) SessionManager.getInstance().getParameter( getString( R.string.payrolls ) );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_saved_paychecks, container, false);
    }

    @Override
    public void onActivityCreated( @Nullable Bundle savedInstanceState ) {

        super.onActivityCreated(savedInstanceState);

        title = ( TextView ) getActivity().findViewById( R.id.title );

        Bundle ba = getArguments();

        folderPaychecks = new File( getActivity().getFilesDir() + File.separator + getString( R.string.paycheck ).toLowerCase() );

        if ( folderPaychecks.exists() ) {

            years = new ArrayList<>();

            for ( File f : folderPaychecks.listFiles( new FileFilter() {

                @Override
                public boolean accept( File pathname ) {
                    return pathname.isDirectory();
                }
            } ) ) {

                for ( BeanAno ano : folhas.getFolhas() ) {

                    if ( ano.getAno().equals( Integer.valueOf( f.getName() ) ) ) {
                        years.add( ano );
                    }
                }
            }
        }

        setList();
    }

    public void onButtonPressed( Uri uri ) {

        if ( listener != null ) {
            listener.onFragmentInteraction( uri );
        }
    }

    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
    }

    @Override
    public void onDetach() {

        super.onDetach();

        listener = null;
    }

    private void setList() {

        if ( fragmentType >= 0 ) {

            switch ( fragmentType ) {

                case FRAGMENT_lIST_YEARS :

                    title.setText( getString( R.string.selectYear ) );

                    setListAdapter(
                            new BeanAnoAdapter( ( AppCompatActivity ) getActivity(), years ) );

                    break;

                case FRAGMENT_lIST_MONTHS :

                    title.setText( getString( R.string.selectMonth ) );

                    setListAdapter(
                            new BeanMesAdapter( ( AppCompatActivity ) getActivity(), months ) );

                    break;

                case FRAGMENT_lIST_PAYROLLS :

                    title.setText( getString( R.string.selectPayroll ) );

                    setListAdapter(
                            new BeanFolhaAdapter( ( AppCompatActivity ) getActivity(), payrolls ) );

                    break;
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        if ( fragmentType >= 0 ) {

            switch ( fragmentType ) {

                case FRAGMENT_lIST_YEARS:

                    year       = ( BeanAno ) getListAdapter().getItem( position );
                    folderYear = new File( folderPaychecks, year.getAno().toString() );

                    months = new ArrayList<>();

                    for ( File f : folderYear.listFiles( new FileFilter() {

                        @Override
                        public boolean accept( File pathname ) {
                            return pathname.isDirectory();
                        }
                    } ) ) {

                        for ( BeanMes mes : year.getMeses() ) {

                            if ( mes.getMes().equals( Integer.valueOf( f.getName() ) ) ) {
                                months.add( mes );
                            }
                        }
                    }

                    fragmentType = FRAGMENT_lIST_MONTHS;

                    setList();

                    break;

                case FRAGMENT_lIST_MONTHS :

                    month       = ( BeanMes ) getListAdapter().getItem( position );
                    folderMonth = new File( folderYear, month.getMes().toString() );

                    payrolls = new ArrayList<>();

                    for ( File f : folderMonth.listFiles( new FileFilter() {

                        @Override
                        public boolean accept( File pathname ) {
                            return pathname.isFile();
                        }
                    } ) ) {

                        for ( BeanFolha folha : month.getFolhas() ) {

                            if ( folha.getIdComp().equals( Long.valueOf( f.getName().substring( 0, f.getName().indexOf( "." ) ) ) ) ) {
                                payrolls.add( folha );
                            }
                        }
                    }

                    fragmentType = FRAGMENT_lIST_PAYROLLS;

                    setList();

                    break;

                case FRAGMENT_lIST_PAYROLLS :

                    payroll = ( BeanFolha ) getListAdapter().getItem( position );

                    for ( File f : folderMonth.listFiles( new FileFilter() {

                        @Override
                        public boolean accept( File pathname ) {
                            return pathname.isFile();
                        }
                    } ) ) {

                        if ( payroll.getIdComp().equals( Long.valueOf( f.getName().substring( 0, f.getName().indexOf( "." ) ) ) ) ) {

                            FileInputStream stream = null;

                            try {

                                data = new StringBuilder();

                                stream = new FileInputStream( f );

                                int ch;

                                while ( ( ch = stream.read() ) != -1 ) {
                                    data.append( ( char ) ch );
                                }
                            }
                            catch ( Exception e ) {
                                e.printStackTrace();
                            }
                            finally {

                                if ( stream != null ) {

                                    try {
                                        stream.close();
                                    }
                                    catch ( Exception e ) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    Intent it = new Intent( getContext(), PaycheckActivity.class );

                    it.putExtra( getString( R.string.id_idComp ), payroll.getIdComp().intValue() );
                    it.putExtra( getContext().getString( R.string.month ), month.getMes() );
                    it.putExtra( getContext().getString( R.string.year ), year.getAno() );
                    it.putExtra( getContext().getString( R.string.payroll ), payroll.getNomeFolha() );
                    it.putExtra( getContext().getString( R.string.paycheck ), data.toString() );
                    it.putExtra( getContext().getString( R.string.saved ), true );

                    getContext().startActivity( it );
            }
        }
    }

    public void setFragmentType( int fragmentType ) {

        this.fragmentType = fragmentType;
        setList();
    }

    public int getCurrentFragmentType() {
        return fragmentType;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction( Uri uri );
    }
}
