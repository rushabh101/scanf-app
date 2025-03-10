package club.devsoc.scanf.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.devsoc.scanf.R
//import club.devsoc.scanf.databinding.ActivityMainBinding
import club.devsoc.scanf.model.PdfModel
import club.devsoc.scanf.view.adapter.PdfRvAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.scanlibrary.ScanConstants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    private var drawerLayout: DrawerLayout? = null
    private lateinit var bottomSheet : ConstraintLayout
    private lateinit var bottomSheetBehavior :
            BottomSheetBehavior<ConstraintLayout>

    private  lateinit var recyclerView: RecyclerView
    private lateinit var pdfAdapter:PdfRvAdapter
    private lateinit var PDFlist:ArrayList<PdfModel>
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        defineLayouts()
        initActivity()
        attachOnClickListeners()

        nav_view.setNavigationItemSelectedListener(this)

        defineLayouts()

        this.drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
//        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
                nav_view
                .setCheckedItem(R.id.nav_recent_files)
        }

        initActivity()

        setRecyclerView()

        attachOnClickListeners()
    }

    private fun setRecyclerView() {
        //get pdf file data
        searchPDF()

        //initialise and set adapter
        pdfAdapter = PdfRvAdapter(PDFlist)
        recyclerView.adapter=pdfAdapter

        //initialise recycle view
        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager=gridLayoutManager
        //notify data set changed
        pdfAdapter.notifyDataSetChanged()
    }

    private fun searchPDF() {
        var root = File(getExternalFilesDir(null), "My PDF Folder")
        val FileList: Array<File>? = root.listFiles()
        PDFlist = ArrayList(3)
        if (FileList != null) {
            for (i in FileList.indices) {
                    if (FileList[i].getName().endsWith(".pdf")) {
                        //here you have that file.
                        var temppdf:PdfModel = PdfModel(FileList[i].path,FileList[i].name)
                        PDFlist.add(temppdf)
                }
            }
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_settings -> {
            }
            R.id.nav_recent_files -> {
            }
            R.id.nav_document_viewer -> {
            }
        }
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    private fun defineLayouts() {
        // defining the bottom sheet layout
        bottomSheet = findViewById(R.id.bottom_sheet_layout)
        recyclerView =findViewById(R.id.main_rv)
        bottomSheet = bottom_sheet_layout
    }

    private fun initActivity() {
        // defining the bottom sheet behaviour
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        // setting initial state of bottom sheet to hidden
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    // attaching on click listeners
    private fun attachOnClickListeners() {
        document_scan_btn.setOnClickListener(this)
        qr_scan_btn.setOnClickListener(this)
        floating_action_button.setOnClickListener(this)
        main_activity_layout.setOnClickListener(this)
    }

    // toggling bottom sheet visibility when fab is pressed
    private fun onFabClicked() {
        if (bottomSheetBehavior.state == 2 ||
            bottomSheetBehavior.state == 5)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        else
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun onQRClicked(){
        val intent = Intent(this, QRScanActivity::class.java)
        startActivity(intent)
    }

    private fun onDocScanClicked(){
        val intentDoc = Intent(this, ImageActivity::class.java)
        startActivity(intentDoc)
    }



    // whenever the user clicks anywhere on the screen, the bottom sheet is hidden
    private fun onScreenClicked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.document_scan_btn -> onDocScanClicked()
            R.id.qr_scan_btn -> onQRClicked()
            R.id.floating_action_button -> onFabClicked()
            R.id.main_activity_layout -> onScreenClicked()
        }
    }
}