package com.brain.multimediapuzzlesviewer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment

internal class ActionsFragment : Fragment() {
    companion object {
        fun newInstance(sectionNumber: Int): ActionsFragment {
            val fragment = ActionsFragment()
            val args = Bundle()
            args.putInt("section_fragment_number", sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}