package info.com;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import info.com.R;

public class MainActivity extends AppCompatActivity implements LocationListener {


    private Button buttonKonumAl;
    private TextView textViewEnlem,textViewBoylam;
    private LocationManager konumYoneticisi;
    private String konumSaglayici = "gps";
    private int izinKontrol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonKonumAl = (Button) findViewById(R.id.buttonKonumAl);
        textViewEnlem = (TextView) findViewById(R.id.textViewEnlem);
        textViewBoylam = (TextView) findViewById(R.id.textViewBoylam);

        konumYoneticisi = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        buttonKonumAl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                izinKontrol = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

                if(izinKontrol != PackageManager.PERMISSION_GRANTED){
                    //uygulamanın manifestinde izin var ama kullanıcı izni onaylamışmı bunun kontrolu yapılır

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);

                    //izin kontrolu daha önce yapılmış ve izine onay verilmemişse , izin alma diyalogu çıkar.
                }else{
                    //daha önce izine onay verilmişse burası çalışır.

                    Location konum = konumYoneticisi.getLastKnownLocation(konumSaglayici);

                    if (konum != null) {

                        onLocationChanged(konum);

                    } else {
                        textViewEnlem.setText("Konum aktif değil");
                        textViewBoylam.setText("Konum aktif değil");
                    }
                }

            }
        });
    }



    @Override
    public void onLocationChanged(Location location) {

        Double enlem = location.getLatitude();
        Double boylam = location.getLongitude();
        //Double yukseklik = location.getAltitude();
        textViewEnlem.setText("Enlem : "+ String.valueOf(enlem));
        textViewBoylam.setText("Boylam : " +String.valueOf(boylam));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {

            izinKontrol = ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "İzin kabul edildi.", Toast.LENGTH_LONG).show();

                Location konum = konumYoneticisi.getLastKnownLocation(konumSaglayici);

                if (konum != null) {
                    System.out.println("Provider " + konumSaglayici + " seçildi.");
                    onLocationChanged(konum);
                } else {
                    textViewEnlem.setText("Konum aktif değil");
                    textViewBoylam.setText("Konum aktif değil");
                }

            } else {
                Toast.makeText(getApplicationContext(), "İzin reddedildi.", Toast.LENGTH_LONG).show();
            }
        }

    }
}


