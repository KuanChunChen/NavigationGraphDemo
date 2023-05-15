package elegant.access.navigationgraphdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import elegant.access.navigationgraphdemo.databinding.FragmentThirdBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root

    }

    private var time: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("ThirdFragment, onViewCreated")

        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                delay(2000)
                val testTime: String = System.currentTimeMillis().toString()
                time = testTime
                println("ThirdFragment, after 2s get new data : $time")

            }
        }

        println("ThirdFragment, store time : $time")
        binding.textviewSecond.text = getString(R.string.third_fragment_label)
        binding.buttonSecond.setOnClickListener {

            findNavController().navigate(R.id.action_ThirdFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        println("onSaveInstanceState : ${System.currentTimeMillis()}")
//        outState.putString("time", System.currentTimeMillis().toString())
    }


}