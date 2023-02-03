package com.ddt.smsalarm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ddt.smsalarm.R
import com.ddt.smsalarm.data.db.FilterEntity
import com.ddt.smsalarm.databinding.BottomsheetAddFilterBinding
import com.ddt.smsalarm.util.setOrClearError
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFilterBottomSheet(val filterId: Int = 0) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetAddFilterBinding
    private val viewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetAddFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //background transparent
        setStyle(STYLE_NORMAL, R.style.custom_bottom_sheet_dialog_theme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()
        setOnclick()
        setTextChangeListener()
    }

    private fun initialize() {

        if (filterId != 0) {
            //edit
            binding.apply {
                tvTitle.text = getString(R.string.edit_filter)
                btnSubmit.text = getString(R.string.submit)
                //initView
                lifecycleScope.launchWhenCreated {
                    viewModel.getFilter(filterId)?.let {
                        etFilterName.setText(it.text)
                    }
                }
            }
        }
    }

    private fun setOnclick() {
        binding.apply {

            ivClose.setOnClickListener {
                dismiss()
            }

            btnSubmit.setOnClickListener {
                if (binding.etFilterName.text?.isNotBlank() == true) {
                    val entity = FilterEntity(
                        id = filterId,
                        text = etFilterName.text.toString().trim(),
                    )

                    viewModel.insertFilter(entity)
                    dismiss()
                } else {
                    tilFilterName.setOrClearError(getString(R.string.enter_filter))
                }
            }
        }
    }

    private fun setTextChangeListener() {
        binding.apply {
            etFilterName.addTextChangedListener {
                if (etFilterName.text.toString().isBlank())
                    tilFilterName.setOrClearError(getString(R.string.enter_filter))
                else
                    tilFilterName.setOrClearError("")
            }
        }
    }
}