package com.dryfire.sold.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dryfire.sold.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private TextInputLayout bidnowInput;
    private TextInputEditText bidnowEdit;
    private MaterialButton bidnowButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.sold_bid_now,container,false);
        bidnowInput = v.findViewById(R.id.sold_bid_now_bidInput);
        bidnowEdit = v.findViewById(R.id.sold_bid_now_bidEdit);
        bidnowButton = v.findViewById(R.id.sold_bid_now_button);
        return v;
    }
}
