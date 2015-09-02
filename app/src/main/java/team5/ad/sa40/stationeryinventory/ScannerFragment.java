package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.livotov.zxscan.ScannerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScannerFragment extends android.support.v4.app.Fragment implements ScannerView.ScannerViewEventListener {

    @Bind(R.id.scanner) ScannerView embeddedScanner;
    @Bind(R.id.rescanBtn) Button btnReScan;
    @Bind(R.id.btnStopScanner) Button btnStopScan;
    @Bind(R.id.itemNumberValue) TextView textItemNumber;
    @Bind(R.id.item_name_value) TextView textItemName;
    @Bind(R.id.category_value) TextView textCategory;
    @Bind(R.id.waitLabel) TextView waitLabel;
    @Bind(R.id.scannerRoot) FrameLayout embeddedScannerRoot;

    //zxscanlib
    private String lastEmbeddedScannerScannedData;
    private long lastEmbeddedScannerScannedDataTimestamp;

    public ScannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        ButterKnife.bind(this, view);
        embeddedScanner.setScannerViewEventListener(this);
        startEmbeddedScanner();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void startEmbeddedScanner()
    {
        embeddedScannerRoot.setVisibility(View.VISIBLE);
        waitLabel.setVisibility(View.VISIBLE);
        embeddedScanner.startScanner();
    }

    private void stopEmbeddedScanner()
    {
        embeddedScanner.stopScanner();
        embeddedScannerRoot.setVisibility(View.INVISIBLE);
    }

    private void displayScannedResult(final String data)
    {
        Toast.makeText(getActivity(), "Data scanned: " + data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScannerReady()
    {
        if (waitLabel.getVisibility() == View.VISIBLE)
        {
            waitLabel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onScannerFailure(int cameraError)
    {
        Toast.makeText(getActivity(), getString(R.string.camera_error, cameraError), Toast.LENGTH_LONG).show();
        startEmbeddedScanner();
    }

    public boolean onCodeScanned(final String data)
    {
        // As we run embedded scanner in continuous mode, we have to add same code protection here in order to avoid
        // generating a lot of same-code scan events
        if (data != null)
        {
            if (data.equalsIgnoreCase(lastEmbeddedScannerScannedData) && System.currentTimeMillis() - lastEmbeddedScannerScannedDataTimestamp < 1000)
            {
                return false;
            }
            else
            {
                displayScannedResult(data);
                lastEmbeddedScannerScannedData = data;
                lastEmbeddedScannerScannedDataTimestamp = System.currentTimeMillis();
                return true;
            }
        }

        return false;
    }
}
