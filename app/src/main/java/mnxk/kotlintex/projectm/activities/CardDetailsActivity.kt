package mnxk.kotlintex.projectm.activities

import android.os.Bundle
import android.view.Menu
import mnxk.kotlintex.projectm.R
import mnxk.kotlintex.projectm.databinding.ActivityCardDetailsBinding
import mnxk.kotlintex.projectm.models.Board
import mnxk.kotlintex.projectm.utils.Constants
import mnxk.kotlintex.projectm.utils.IntentUtils

class CardDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityCardDetailsBinding
    private lateinit var boardDetails: Board
    private var taskListPosition: Int = -1
    private var cardPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setupActionbar()
        setupUI()
    }

    private fun setupUI() {
        binding.etNameCardDetails.setText(boardDetails.taskList[taskListPosition].cards[cardPosition].name)
        binding.etNameCardDetails.setSelection(binding.etNameCardDetails.text.toString().length)
    }

    private fun setupActionbar() {
        setSupportActionBar(binding.toolbarCardDetailsActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = boardDetails.taskList[taskListPosition].cards[cardPosition].name
        }
        onBackPress()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun getIntentData() {
        if (intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)) {
            taskListPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)) {
            cardPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.BOARD_DETAIL)) {
            boardDetails = IntentUtils.getParcelableExtra<Board>(intent, Constants.BOARD_DETAIL)!!
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
