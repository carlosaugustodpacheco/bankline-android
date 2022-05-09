package me.dio.bankline.ui.statement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import me.dio.bankline.R
import me.dio.bankline.data.State
import me.dio.bankline.databinding.ActivityBankStatementBinding
import me.dio.bankline.domain.Correntista
import me.dio.bankline.domain.Movimentacao
import me.dio.bankline.domain.TipoMovimentacao
import java.lang.IllegalArgumentException

class BankStatementActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_ACCOUNT_HOLDER = "me.dio.bankline.ui.statement.EXTRA_ACCOUNT_HOLDER"
    }
    private  val binding by lazy {
        ActivityBankStatementBinding.inflate(layoutInflater)
    }
    private  val accountHolder by lazy {
        intent.getParcelableExtra<Correntista>(EXTRA_ACCOUNT_HOLDER) ?: throw IllegalArgumentException()
    }

    private val viewModel by viewModels<BankStatementViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvBankStatement.layoutManager = LinearLayoutManager(this)

        findBankStatement()

        binding.srlBankStatement.setOnRefreshListener {findBankStatement() }
    }

    private fun findBankStatement() {
    // MOCKANDO A MOVIMENTACAO
        //     val  dataSet = ArrayList <Movimentacao>()
        //dataSet.add(Movimentacao(id = 1, dataHora = "03/05/2022 09:24:55","Lorem Ipsum", 1000.0, TipoMovimentacao.RECEITA, idCorrentista = 2))
        //dataSet.add(Movimentacao(id = 1, dataHora = "03/05/2022 09:24:55","Lorem Ipsum", 1000.0, TipoMovimentacao.DESPESA, idCorrentista = 2))
        //dataSet.add(Movimentacao(id = 1, dataHora = "03/05/2022 09:24:55","Lorem Ipsum", 1000.0, TipoMovimentacao.DESPESA, idCorrentista = 2))
        //dataSet.add(Movimentacao(id = 1, dataHora = "03/05/2022 09:24:55","Lorem Ipsum", 1000.0, TipoMovimentacao.RECEITA, idCorrentista = 2))
        //dataSet.add(Movimentacao(id = 1, dataHora = "03/05/2022 09:24:55","Lorem Ipsum", 1000.0, TipoMovimentacao.RECEITA, idCorrentista = 2))
        //binding.rvBankStatement.adapter = BankStatementAdapter(dataSet)

        viewModel.findBankStatement(accountHolder.id).observe(this) { state ->
            when(state) {
                is State.Success -> {
                    binding.rvBankStatement.adapter = state.data?.let { BankStatementAdapter(it) }
                    binding.srlBankStatement.isRefreshing = false
                }
                is State.Error -> {
                    state.message?.let { Snackbar.make(binding.rvBankStatement, it, Snackbar.LENGTH_LONG).show() }
                    binding.srlBankStatement.isRefreshing = false
                }
                    State.Wait -> binding.srlBankStatement.isRefreshing = true
            }
        }

    }
}