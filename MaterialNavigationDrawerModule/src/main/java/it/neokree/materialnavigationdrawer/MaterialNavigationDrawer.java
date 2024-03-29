package it.neokree.materialnavigationdrawer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.MaterialSubheader;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import it.neokree.materialnavigationdrawer.util.DeviceInfoUtil;
import it.neokree.materialnavigationdrawer.util.MaterialDrawerLayout;
import it.neokree.materialnavigationdrawer.util.TypefaceManager;
import it.neokree.materialnavigationdrawer.util.Utils;

/**
 * Activity that extends ActionBarActivity with a Navigation Drawer with Material Design style
 *
 * @author created by neokree
 */
@SuppressWarnings("unused")
@SuppressLint("InflateParams")
public abstract class MaterialNavigationDrawer<Fragment> extends ActionBarActivity implements MaterialSectionListener, MaterialAccount.OnAccountDataLoaded {

    public static final int BOTTOM_SECTION_START = 100;
    private static final int USER_CHANGE_TRANSITION = 500;

    public static final int BACKPATTERN_BACK_ANYWHERE = 0;
    public static final int BACKPATTERN_BACK_TO_FIRST = 1;
    public static final int BACKPATTERN_CUSTOM = 2;

    private static final int DRAWERHEADER_ACCOUNTS = 0;
    private static final int DRAWERHEADER_IMAGE = 1;
    private static final int DRAWERHEADER_CUSTOM = 2;
    private static final int DRAWERHEADER_NO_HEADER = 3;

    private static final int ELEMENT_TYPE_SECTION = 0;
    private static final int ELEMENT_TYPE_DIVISOR = 1;
    private static final int ELEMENT_TYPE_SUBHEADER = 2;
    private static final int ELEMENT_TYPE_BOTTOM_SECTION = 3;

    private static final String STATE_SECTION = "section";
    private static final String STATE_ACCOUNT = "account";

    private boolean flag = false;

    protected MaterialDrawerLayout layout;
    private ActionBar actionBar;
    private ActionBarDrawerToggle pulsante;
    private ImageView statusBar;
    protected Toolbar toolbar;
    private RelativeLayout content;
    protected RelativeLayout drawer;
    protected ImageView userphoto;
    private ImageView userSecondPhoto;
    private ImageView userThirdPhoto;
    private ImageView usercover;
    private ImageView usercoverSwitcher;
    private TextView username;
    private TextView usermail;
    private ImageButton userButtonSwitcher;
    private LinearLayout customDrawerHeader;
    private LinearLayout sections;
    private LinearLayout bottomSections;

    // Lists
    private List<MaterialSection> sectionList;
    private List<MaterialSection> bottomSectionList;
    private List<MaterialAccount> accountManager;
    private List<MaterialSection> accountSectionList;
    private List<MaterialSubheader> subheaderList;
    private List<Integer> elementsList;
    private List<Fragment> childFragmentStack;
    private List<String> childTitleStack;
    private View globalView;

    // current pointers
    private MaterialSection currentSection;
    protected MaterialAccount currentAccount;

    private String title;
    private float density;
    private int primaryColor;
    private int primaryDarkColor;
    private int drawerColor;
    protected boolean drawerTouchLocked = false;
    protected boolean slidingDrawerEffect = false;
    private boolean multiPaneSupport;
    private boolean rippleSupport;
    private boolean uniqueToolbarColor;
    private boolean singleAccount;
    private boolean accountSwitcher = false;
    protected boolean isCurrentFragmentChild = false;
    private boolean kitkatTraslucentStatusbar = false;
    private static boolean learningPattern = true;
    private int backPattern = BACKPATTERN_BACK_ANYWHERE;
    private int drawerHeaderType;

    // resources
    private Resources resources;
    private TypefaceManager fontManager;

    protected View.OnClickListener currentAccountListener;
    private View.OnClickListener secondAccountListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!drawerTouchLocked) {

                // account change
                MaterialAccount account = findAccountNumber(MaterialAccount.SECOND_ACCOUNT);
                if (account != null) {
                    if (accountListener != null)
                        accountListener.onChangeAccount(account);

                    switchAccounts(account);
                } else {// if there is no second account user clicked for open it
                    accountListener.onAccountOpening(currentAccount);
                    if (!deviceSupportMultiPane())
                        layout.closeDrawer(drawer);
                }
            }

        }
    };
    private View.OnClickListener thirdAccountListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!drawerTouchLocked) {

                // account change
                MaterialAccount account = findAccountNumber(MaterialAccount.THIRD_ACCOUNT);
                if (account != null) {
                    if (accountListener != null)
                        accountListener.onChangeAccount(account);

                    switchAccounts(account);
                } else {// if there is no second account user clicked for open it
                    accountListener.onAccountOpening(currentAccount);
                    if (!deviceSupportMultiPane())
                        layout.closeDrawer(drawer);
                }
            }
        }
    };
    private View.OnClickListener accountSwitcherListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ////切换用户暂时不需要处理
//            if (!drawerTouchLocked) {
//
//                // si rimuovono le viste
//                sections.removeAllViews();
//                bottomSections.removeAllViews();
//
//                if (!accountSwitcher) {
//                    // si cambia l'icona del pulsante
//                    userButtonSwitcher.setImageResource(R.drawable.ic_arrow_drop_up_white_24dp);
//
//                    for (MaterialAccount account : accountManager) {
//                        // si inseriscono tutti gli account ma non quello attualmente in uso
//                        if (account.getAccountNumber() != MaterialAccount.FIRST_ACCOUNT) {
//                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (56 * density));
//                            sections.addView(account.getSectionView(MaterialNavigationDrawer.this, fontManager.getRobotoMedium(), defaultSectionListener, rippleSupport, account.getAccountNumber()), params);
//                        }
//                    }
//                    for (MaterialSection section : accountSectionList) {
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (48 * density));
//                        sections.addView(section.getView(), params);
//                    }
//
//                    accountSwitcher = true; // si attiva l'account switcher per annotare che si visualizzano gli account.
//                } else {
//                    userButtonSwitcher.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp);
//
//                    int indexSection = 0, indexSubheader = 0;
//                    for (int type : elementsList) {
//                        switch (type) {
//                            case ELEMENT_TYPE_SECTION:
//                                MaterialSection section = sectionList.get(indexSection);
//                                indexSection++;
//                                LinearLayout.LayoutParams paramSection = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (48 * density));
//                                sections.addView(section.getView(), paramSection);
//                                break;
//                            case ELEMENT_TYPE_DIVISOR:
//                                View view = new View(MaterialNavigationDrawer.this);
//                                view.setBackgroundColor(Color.parseColor("#e0e0e0"));
//                                // height 1 px
//                                LinearLayout.LayoutParams paramDivisor = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
//                                paramDivisor.setMargins(0, (int) (8 * density), 0, (int) (8 * density));
//                                sections.addView(view, paramDivisor);
//                                break;
//                            case ELEMENT_TYPE_SUBHEADER:
//                                MaterialSubheader subheader = subheaderList.get(indexSubheader);
//                                indexSubheader++;
//                                sections.addView(subheader.getView());
//                                break;
//                            case ELEMENT_TYPE_BOTTOM_SECTION:
//                                break; // le bottom section vengono gestite dopo l'inserimento degli altri elementi
//                        }
//                    }
//
//                    int width = drawer.getWidth();
//                    int heightCover;
//                    switch (drawerHeaderType) {
//                        default:
//                        case DRAWERHEADER_ACCOUNTS:
//                        case DRAWERHEADER_IMAGE:
//                        case DRAWERHEADER_CUSTOM:
//                            // si fa il rapporto in 16 : 9
//                            heightCover = (9 * width) / 16;
//                            break;
//                        case DRAWERHEADER_NO_HEADER:
//                            // height cover viene usato per prendere l'altezza della statusbar
//                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && !kitkatTraslucentStatusbar)) {
//                                heightCover = 0;
//                            } else {
//                                // su kitkat (con il traslucentstatusbar attivo) e su Lollipop e' 25 dp
//                                heightCover = (int) (25 * density);
//                            }
//                            break;
//                    }
//
//                    int heightDrawer = (int) (((8 + 8 + 1) * density) + heightCover + sections.getHeight() + ((density * 48) * bottomSectionList.size()) + (subheaderList.size() * (35 * density)));
//
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && !kitkatTraslucentStatusbar)) {
//                        heightDrawer += (density * 25);
//                    }
//
//                    View divisor = new View(MaterialNavigationDrawer.this);
//                    divisor.setBackgroundColor(Color.parseColor("#e0e0e0"));
//
//                    // si aggiungono le bottom sections
//                    if (heightDrawer >= getHeight()) {
//
//                        LinearLayout.LayoutParams paramDivisor = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
//                        paramDivisor.setMargins(0, (int) (8 * density), 0, (int) (8 * density));
//                        sections.addView(divisor, paramDivisor);
//
//                        for (MaterialSection section : bottomSectionList) {
//                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (48 * density));
//                            sections.addView(section.getView(), params);
//                        }
//                    } else {
//                        LinearLayout.LayoutParams paramDivisor = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
//                        bottomSections.addView(divisor, paramDivisor);
//
//                        for (MaterialSection section : bottomSectionList) {
//                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (48 * density));
//                            bottomSections.addView(section.getView(), params);
//                        }
//                    }
//
//                    accountSwitcher = false;
//                }
//
//            }
        }
    };
    private MaterialSectionListener defaultSectionListener = new MaterialSectionListener() {
        @Override
        public void onClick(MaterialSection section) {
            section.unSelect(); // remove the selected color

            if (!drawerTouchLocked) {
                int accountPosition = section.getPosition();


                MaterialAccount account = findAccountNumber(accountPosition);

                // switch accounts position
                currentAccount.setAccountNumber(accountPosition);
                account.setAccountNumber(MaterialAccount.FIRST_ACCOUNT);
                currentAccount = account;

                notifyAccountDataChanged();

                // call change account method
                if (accountListener != null)
                    accountListener.onChangeAccount(account);

                // change account list
                accountSwitcherListener.onClick(null);

                // close drawer
                if (!deviceSupportMultiPane())
                    layout.closeDrawer(drawer);


            }
        }
    };
    protected View.OnClickListener toolbarToggleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isCurrentFragmentChild) {
                onHomeAsUpSelected();
                onBackPressed();
            }
        }
    };
    protected MaterialAccountListener accountListener;
    protected DrawerLayout.DrawerListener drawerListener;
    private View mCommonHeaderHome;
    public static TextView mCommonHeaderTitle;
    public static View mCommonHeaderSearch;


    @SuppressWarnings("unchecked")
    @Override
    /**
     * Do not Override this method!!! <br>
     * Use init() instead
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources.Theme theme = this.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.drawerType, typedValue, true);
        drawerHeaderType = typedValue.data;
        //drawerHeaderType = 0;
        theme.resolveAttribute(R.attr.rippleBackport, typedValue, false);
        rippleSupport = typedValue.data != 0;
        theme.resolveAttribute(R.attr.uniqueToolbarColor, typedValue, false);
        uniqueToolbarColor = typedValue.data != 0;
        theme.resolveAttribute(R.attr.singleAccount, typedValue, false);
        singleAccount = typedValue.data != 0;
        theme.resolveAttribute(R.attr.multipaneSupport, typedValue, false);
        multiPaneSupport = typedValue.data != 0;
        theme.resolveAttribute(R.attr.drawerColor, typedValue, true);
        drawerColor = typedValue.data;

        if (drawerHeaderType == DRAWERHEADER_ACCOUNTS)
            setContentView(R.layout.activity_material_navigation_drawer);
        else
            setContentView(R.layout.activity_material_navigation_drawer_customheader);

        // init Typeface
        fontManager = new TypefaceManager(this.getAssets());

        // init toolbar & status bar
        statusBar = (ImageView) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mCommonHeaderHome=findViewById(R.id.mCommonHeaderHome);
        mCommonHeaderTitle= (TextView) findViewById(R.id.mCommonHeaderTitle);
        mCommonHeaderSearch=findViewById(R.id.mCommonHeaderSearch);
        mCommonHeaderHome.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                layout.openDrawer(drawer);
            }
        });
        // init drawer components
        layout = (MaterialDrawerLayout) this.findViewById(R.id.drawer_layout);//最外层MaterialDrawer
        content = (RelativeLayout) this.findViewById(R.id.content);//主布局
        drawer = (RelativeLayout) this.findViewById(R.id.drawer);//左边侧边栏

        //判断是否带header布局
        if (drawerHeaderType == DRAWERHEADER_ACCOUNTS) {
            username = (TextView) this.findViewById(R.id.user_nome);
            usermail = (TextView) this.findViewById(R.id.user_email);
            userphoto = (ImageView) this.findViewById(R.id.user_photo);
            userSecondPhoto = (ImageView) this.findViewById(R.id.user_photo_2);
            userThirdPhoto = (ImageView) this.findViewById(R.id.user_photo_3);
            usercover = (ImageView) this.findViewById(R.id.user_cover);
            usercoverSwitcher = (ImageView) this.findViewById(R.id.user_cover_switcher);
            userButtonSwitcher = (ImageButton) this.findViewById(R.id.user_switcher);

            // set Roboto Fonts
            username.setTypeface(fontManager.getRobotoMedium());
            usermail.setTypeface(fontManager.getRobotoRegular());

            // set the image
            if (!singleAccount) {
                userButtonSwitcher.setImageResource(R.drawable.ic_arrow_drop_down_white_24dp);
                userButtonSwitcher.setOnClickListener(accountSwitcherListener);
            }

        } else
            customDrawerHeader = (LinearLayout) this.findViewById(R.id.drawer_header);


        //侧边栏的选项列表
        sections = (LinearLayout) this.findViewById(R.id.sections);
        //侧边栏底部选项列表
        bottomSections = (LinearLayout) this.findViewById(R.id.bottom_sections);

        // init lists
        sectionList = new LinkedList<>();
        bottomSectionList = new LinkedList<>();
        accountManager = new LinkedList<>();
        accountSectionList = new LinkedList<>();
        subheaderList = new LinkedList<>();
        elementsList = new LinkedList<>();
        childFragmentStack = new LinkedList<>();
        childTitleStack = new LinkedList<>();

        // init listeners
        if (drawerHeaderType == DRAWERHEADER_ACCOUNTS) {
            userphoto.setOnClickListener(currentAccountListener);
            if (singleAccount)

                usercover.setOnClickListener(currentAccountListener);
            else
                usercover.setOnClickListener(accountSwitcherListener);
            userSecondPhoto.setOnClickListener(secondAccountListener);
            userThirdPhoto.setOnClickListener(thirdAccountListener);
        }

        // set drawer backgrond color
        drawer.setBackgroundColor(drawerColor);

        //get resources and density
        resources = this.getResources();
        density = resources.getDisplayMetrics().density;

        // set the right drawer width
        DrawerLayout.LayoutParams drawerParams = (android.support.v4.widget.DrawerLayout.LayoutParams) drawer.getLayoutParams();
        drawerParams.width = Utils.getDrawerWidth(resources);
        drawer.setLayoutParams(drawerParams);

        // get primary color
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        primaryColor = typedValue.data;
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        primaryDarkColor = typedValue.data;

        // if device is kitkat
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            TypedArray windowTraslucentAttribute = theme.obtainStyledAttributes(new int[]{android.R.attr.windowTranslucentStatus});
            kitkatTraslucentStatusbar = windowTraslucentAttribute.getBoolean(0, false);
            if (kitkatTraslucentStatusbar) {
                Window window = this.getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                RelativeLayout.LayoutParams statusParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.traslucentStatusMargin));
                statusBar.setLayoutParams(statusParams);
                statusBar.setImageDrawable(new ColorDrawable(darkenColor(primaryColor)));

                if (drawerHeaderType == DRAWERHEADER_ACCOUNTS) {
                    RelativeLayout.LayoutParams photoParams = (RelativeLayout.LayoutParams) userphoto.getLayoutParams();
                    photoParams.setMargins((int) (16 * density), resources.getDimensionPixelSize(R.dimen.traslucentPhotoMarginTop), 0, 0);
                    userphoto.setLayoutParams(photoParams);
                }
            }
        }
        // INIT ACTION BAR
        this.setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        // DEVELOPER CALL TO INIT
        init(savedInstanceState);

        if (sectionList.size() == 0) {
            throw new RuntimeException("You must add at least one Section to top list.");
        }

        if (deviceSupportMultiPane()) {
            // se il multipane e' attivo, si e' in landscape e si e' un tablet allora si passa in multipane mode
            layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, drawer);
            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins((int) (320 * density), 0, 0, 0);
            content.setLayoutParams(params);
            layout.setScrimColor(Color.TRANSPARENT);
            layout.openDrawer(drawer);
            layout.requestDisallowInterceptTouchEvent(true);
        } else {
            // se non si sta lavorando in multiPane allora si inserisce il pulsante per aprire/chiudere

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            pulsante = new ActionBarDrawerToggle(this, layout, toolbar, R.string.nothing, R.string.nothing) {

                public void onDrawerClosed(View view) {
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

                    // abilita il touch del drawer
                    drawerTouchLocked = false;
                    setSectionsTouch(true);

                    if (drawerListener != null)
                        drawerListener.onDrawerClosed(view);

                    if (flag) {
                        globalView.setVisibility(View.VISIBLE);
                    }

                }

                public void onDrawerOpened(View drawerView) {
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

                    if (drawerListener != null)
                        drawerListener.onDrawerOpened(drawerView);

                    globalView.setVisibility(View.GONE);

                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {


                    if (!isCurrentFragmentChild) { // if user seeing a master fragment

                        // if user wants the sliding arrow it compare
                        if (slidingDrawerEffect) {
                            super.onDrawerSlide(drawerView, slideOffset);
                            globalView.setVisibility(View.GONE);
                        }
                        else {
                            super.onDrawerSlide(drawerView, 0);

                        }
                    } else {// if user seeing a child fragment always shows the back arrow
                        super.onDrawerSlide(drawerView, 1f);
                    }

                    if (drawerListener != null)
                        drawerListener.onDrawerSlide(drawerView, slideOffset);



                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    super.onDrawerStateChanged(newState);

                    if (drawerListener != null)
                        drawerListener.onDrawerStateChanged(newState);
                }
            };
            pulsante.setToolbarNavigationClickListener(toolbarToggleListener);

            layout.setDrawerListener(pulsante);
            layout.requestDisallowInterceptTouchEvent(false);
        }

        // si attacca alla usercover un listener
        ViewTreeObserver vto;
        if (drawerHeaderType == DRAWERHEADER_ACCOUNTS)
            vto = usercover.getViewTreeObserver();
        else
            vto = customDrawerHeader.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // quando l'immagine e' stata caricata

                // change user space to 16:9
                int width = drawer.getWidth();

                int heightCover;
                switch (drawerHeaderType) {
                    default:
                    case DRAWERHEADER_ACCOUNTS:
                    case DRAWERHEADER_IMAGE:
                    case DRAWERHEADER_CUSTOM:
                        // si fa il rapporto in 16 : 9
                        heightCover = (9 * width) / 16;
                        break;
                    case DRAWERHEADER_NO_HEADER:
                        // height cover viene usato per prendere l'altezza della statusbar
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && !kitkatTraslucentStatusbar)) {
                            heightCover = 0;
                        } else {
                            // su kitkat (con il traslucentstatusbar attivo) e su Lollipop e' 25 dp
                            heightCover = (int) (25 * density);
                        }
                        break;
                }

                // set user space
                if (drawerHeaderType == DRAWERHEADER_ACCOUNTS) {
                    usercover.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightCover));
                    usercoverSwitcher.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightCover));
                } else {
                    customDrawerHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightCover));
                }

                //  heightCover (DRAWER HEADER) + 8 (PADDING) + sections + 8 (PADDING) + 1 (DIVISOR) + bottomSections + subheaders
                int heightDrawer = (int) (((8 + 8 + 1) * density) + heightCover + sections.getHeight() + ((density * 48) * bottomSectionList.size()) + (subheaderList.size() * (35 * density)));

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && !kitkatTraslucentStatusbar)) {
                    heightDrawer += (density * 25);
                }

                // create the divisor
                View divisor = new View(MaterialNavigationDrawer.this);
                divisor.setBackgroundColor(Color.parseColor("#e0e0e0"));

                // si aggiungono le bottom sections
                if (heightDrawer >= getHeight()) {

                    // add the divisor to the section view
                    LinearLayout.LayoutParams paramDivisor = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    paramDivisor.setMargins(0, (int) (8 * density), 0, (int) (8 * density));
                    sections.addView(divisor, paramDivisor);


                    for (MaterialSection section : bottomSectionList) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (48 * density));
                        sections.addView(section.getView(), params);
                    }
                } else {
                    // add the divisor to the bottom section listview
                    LinearLayout.LayoutParams paramDivisor = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    bottomSections.addView(divisor, paramDivisor);

                    for (MaterialSection section : bottomSectionList) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (48 * density));
                        bottomSections.addView(section.getView(), params);
                    }
                }


                ViewTreeObserver obs;
                if (drawerHeaderType == DRAWERHEADER_ACCOUNTS)
                    obs = usercover.getViewTreeObserver();
                else
                    obs = customDrawerHeader.getViewTreeObserver();

                // si rimuove il listener
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });

        MaterialSection section;
        if (savedInstanceState == null) {

            // init account views
            if (accountManager.size() > 0) {
                currentAccount = accountManager.get(0);
                notifyAccountDataChanged();
            }

            // init section
            section = sectionList.get(0);
            if (section.getTarget() != MaterialSection.TARGET_FRAGMENT)
                throw new RuntimeException("The first section added must have a fragment as target");
        } else {

            ArrayList<Integer> accountNumbers = savedInstanceState.getIntegerArrayList(STATE_ACCOUNT);

            // ripristina gli account
            for (int i = 0; i < accountNumbers.size(); i++) {
                MaterialAccount account = accountManager.get(i);
                account.setAccountNumber(accountNumbers.get(i));
                if (account.getAccountNumber() == MaterialAccount.FIRST_ACCOUNT) {
                    currentAccount = account;
                }
            }

            notifyAccountDataChanged();

            int accountSelected = savedInstanceState.getInt(STATE_SECTION);

            if (accountSelected >= BOTTOM_SECTION_START) {
                section = bottomSectionList.get(accountSelected - BOTTOM_SECTION_START);
            } else
                section = sectionList.get(accountSelected);

            if (section.getTarget() != MaterialSection.TARGET_FRAGMENT) {
                section = sectionList.get(0);
            }

        }
        title = section.getTitle();
        //title = "你未见的时代痛楚";
//        section.setTitle(title);
        currentSection = section;
        section.select();
        setFragment((Fragment) section.getTargetFragment(), title, null, savedInstanceState != null);

        // change the toolbar color for the first section
        changeToolbarColor(section);

        // add the first section to the child stack
        childFragmentStack.add((Fragment) section.getTargetFragment());
        childTitleStack.add(section.getTitle());

        // learning pattern
        if (learningPattern) {
            layout.openDrawer(drawer);
            disableLearningPattern();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!deviceSupportMultiPane())
            layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, drawer);
        else
            layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, drawer);
    }


    // Gestione dei Menu -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    /* Chiamata dopo l'invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (layout.isDrawerOpen(drawer)) {
            menu.clear();
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se dal drawer si seleziona un oggetto
        if (pulsante != null)
            if (pulsante.onOptionsItemSelected(item)) {
                return true;
            }
        return super.onOptionsItemSelected(item);
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (pulsante != null)
            pulsante.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {// al cambio di orientamento dello schermo
        super.onConfigurationChanged(newConfig);

        // Passa tutte le configurazioni al drawer
        if (pulsante != null) {
            pulsante.onConfigurationChanged(newConfig);
        }
    }

    public void setTitle(String title) {
        this.title = title;
        this.getSupportActionBar().setTitle(title);
    }

    //设置view
    public void setGlobalView(View globalView) {
        this.globalView = globalView;
    }

    public void setGlobalFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void onBackPressed() {
        if (!isCurrentFragmentChild) {
            switch (backPattern) {
                default:
                case BACKPATTERN_BACK_ANYWHERE:
                    super.onBackPressed();
                    break;
                case BACKPATTERN_BACK_TO_FIRST:
                    MaterialSection section = sectionList.get(0);
                    if (currentSection == section)
                        super.onBackPressed();
                    else {
                        section.select();
                        onClick(section);
                    }
                    break;
                case BACKPATTERN_CUSTOM:
                    MaterialSection backedSection = backToSection(getCurrentSection());

                    if (currentSection == backedSection)
                        super.onBackPressed();
                    else {
                        if (backedSection.getTarget() != MaterialSection.TARGET_FRAGMENT) {
                            throw new RuntimeException("The restored section must have a fragment as target");
                        }
                        backedSection.select();
                        onClick(backedSection);
                    }
                    break;
            }
        } else {
            if (childFragmentStack.size() <= 1) {
                isCurrentFragmentChild = false;
                onBackPressed();
            } else {
                // reload the child before
                Fragment newFragment = childFragmentStack.get(childFragmentStack.size() - 2);
                String newTitle = childTitleStack.get(childTitleStack.size() - 2);

                // get and remove the last child
                Fragment currentFragment = childFragmentStack.remove(childFragmentStack.size() - 1);
                childTitleStack.remove(childTitleStack.size() - 1);

                setFragment(newFragment, newTitle, currentFragment);

                if (childFragmentStack.size() == 1) {
                    // user comed back to master section
                    isCurrentFragmentChild = false;
                    pulsante.setDrawerIndicatorEnabled(true);
                }
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        int position = this.getCurrentSection().getPosition();
        outState.putInt(STATE_SECTION, position);
        ArrayList<Integer> list = new ArrayList<>();
        for (MaterialAccount account : accountManager)
            list.add(account.getAccountNumber());
        outState.putIntegerArrayList(STATE_ACCOUNT, list);

        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // recycle bitmaps
        recycleAccounts();
    }

    /**
     * Method used with BACKPATTERN_CUSTOM to retrieve the section which is restored
     *
     * @param currentSection
     * @return the Section to restore that has Fragment as target (or currentSection for exit from activity)
     */
    protected MaterialSection backToSection(MaterialSection currentSection) {
        return currentSection;
    }

    public void setFragment(Fragment fragment, String title) {
        setFragment(fragment, title, null);

        if (!isCurrentFragmentChild) {// remove the last child from the stack
            childFragmentStack.remove(childFragmentStack.size() - 1);
            childTitleStack.remove(childTitleStack.size() - 1);
        } else
            for (int i = childFragmentStack.size() - 1; i >= 0; i++) { // if a section is clicked when user is into a child remove all childs from stack
                childFragmentStack.remove(i);
                childTitleStack.remove(i);
            }

        // add to the childStack the Fragment and title
        childFragmentStack.add(fragment);
        childTitleStack.add(title);

        isCurrentFragmentChild = false;
    }

    private void setFragment(Fragment fragment, String title, Fragment oldFragment) {
        setFragment(fragment, title, oldFragment, false);
    }

    /**
     * TODO
     *
     * @param fragment
     * @param title
     * @param oldFragment
     */
    private void setFragment(Fragment fragment, String title, Fragment oldFragment, boolean hasSavedInstanceState) {
        // si setta il titolo
        setTitle(title);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // before honeycomb there is not android.app.Fragment
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (oldFragment != null && oldFragment != fragment)
                ft.remove((android.support.v4.app.Fragment) oldFragment);

            if (!hasSavedInstanceState) // se non e' avvenuta una rotazione
                ft.replace(R.id.frame_container, (android.support.v4.app.Fragment) fragment).commit();
        } else if (fragment instanceof android.app.Fragment) {
            if (oldFragment instanceof android.support.v4.app.Fragment)
                throw new RuntimeException("You should use only one type of Fragment");


            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (oldFragment != null && fragment != oldFragment)
                ft.remove((android.app.Fragment) oldFragment);

            if (!hasSavedInstanceState) // se non e' avvenuta una rotazione
                ft.replace(R.id.frame_container, (android.app.Fragment) fragment).commit();
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            if (oldFragment instanceof android.app.Fragment)
                throw new RuntimeException("You should use only one type of Fragment");

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (oldFragment != null && oldFragment != fragment)
                ft.remove((android.support.v4.app.Fragment) oldFragment);

            if (!hasSavedInstanceState) // se non e' avvenuta una rotazione
                ft.replace(R.id.frame_container, (android.support.v4.app.Fragment) fragment).commit();
        } else
            throw new RuntimeException("Fragment must be android.app.Fragment or android.support.v4.app.Fragment");

        // si chiude il drawer
        if (!deviceSupportMultiPane())
            layout.closeDrawer(drawer);
    }

    public void setFragmentChild(Fragment fragment, String title) {
        isCurrentFragmentChild = true;

        // replace the fragment
        setFragment(fragment, title, childFragmentStack.get(childFragmentStack.size() - 1));

        // add to the stack the child
        childFragmentStack.add(fragment);
        childTitleStack.add(title);

        // sync the toolbar toggle state
        pulsante.setDrawerIndicatorEnabled(false);
    }

    private MaterialAccount findAccountNumber(int number) {
        for (MaterialAccount account : accountManager)
            if (account.getAccountNumber() == number)
                return account;


        return null;
    }

    private void switchAccounts(final MaterialAccount newAccount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            final ImageView floatingImage = new ImageView(this);

            // si calcolano i rettangoli di inizio e fine
            Rect startingRect = new Rect();
            Rect finalRect = new Rect();
            Point offsetHover = new Point();

            // 64dp primary user image / 40dp other user image = 1.6 scale
            float finalScale = 1.6f;

            final int statusBarHeight;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT && !kitkatTraslucentStatusbar)) {
                statusBarHeight = (int) (25 * density);
            } else {
                statusBarHeight = 0;
            }

            // si tiene traccia della foto cliccata
            ImageView photoClicked;
            if (newAccount.getAccountNumber() == MaterialAccount.SECOND_ACCOUNT) {
                photoClicked = userSecondPhoto;
            } else {
                photoClicked = userThirdPhoto;
            }
            photoClicked.getGlobalVisibleRect(startingRect, offsetHover);
            floatingImage.setImageDrawable(photoClicked.getDrawable());

            // si aggiunge una view nell'esatta posizione dell'altra
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(photoClicked.getWidth(), photoClicked.getHeight());
            params.setMargins(offsetHover.x, offsetHover.y - statusBarHeight, 0, 0);
            drawer.addView(floatingImage, params);

            // si setta la nuova foto di profilo sopra alla vecchia
            photoClicked.setImageDrawable(currentAccount.getCircularPhoto());

            // si setta la nuova immagine di background da visualizzare sotto la vecchia
            usercoverSwitcher.setImageDrawable(newAccount.getBackground());

            userphoto.getGlobalVisibleRect(finalRect);

            // Si calcola l'offset finale (LARGHEZZA DEL CONTAINER GRANDE - LARGHEZZA DEL CONTAINER PICCOLO / 2) e lo si applica
            int offset = (((finalRect.bottom - finalRect.top) - (startingRect.bottom - finalRect.top)) / 2);
            finalRect.offset(offset, offset - statusBarHeight);
            startingRect.offset(0, -statusBarHeight);


            // Se il dispositivo usa un linguaggio RTL si rimuove l'offset della parte a sinistra dello schermo
            if (Utils.isRTL()) {
                // si rimuove dal conteggio la parte a sinistra del drawer.
                int leftOffset = resources.getDisplayMetrics().widthPixels - Utils.getDrawerWidth(resources);

                startingRect.left -= leftOffset;
                finalRect.left -= leftOffset;
            }

            // si animano le viste
            AnimatorSet set = new AnimatorSet();
            set
                    // si ingrandisce l'immagine e la si sposta a sinistra.
                    .play(ObjectAnimator.ofFloat(floatingImage, View.X, startingRect.left, finalRect.left))
                    .with(ObjectAnimator.ofFloat(floatingImage, View.Y, startingRect.top, finalRect.top))
                    .with(ObjectAnimator.ofFloat(floatingImage, View.SCALE_X, 1f, finalScale))
                    .with(ObjectAnimator.ofFloat(floatingImage, View.SCALE_Y, 1f, finalScale))
                    .with(ObjectAnimator.ofFloat(userphoto, View.ALPHA, 1f, 0f))
                    .with(ObjectAnimator.ofFloat(usercover, View.ALPHA, 1f, 0f))
                    .with(ObjectAnimator.ofFloat(photoClicked, View.SCALE_X, 0f, 1f))
                    .with(ObjectAnimator.ofFloat(photoClicked, View.SCALE_Y, 0f, 1f));
            set.setDuration(USER_CHANGE_TRANSITION);
            set.setInterpolator(new DecelerateInterpolator());
            set.addListener(new AnimatorListenerAdapter() {

                @SuppressLint("NewApi")
                @Override
                public void onAnimationEnd(Animator animation) {

                    // si carica la nuova immagine
                    ((View) userphoto).setAlpha(1);
                    setFirstAccountPhoto(newAccount.getCircularPhoto());

                    // si cancella l'imageview per l'effetto
                    drawer.removeView(floatingImage);

                    // si cambiano i dati utente
                    setUserEmail(newAccount.getSubTitle());
                    setUsername(newAccount.getTitle());

                    // si cambia l'immagine soprastante
                    setDrawerHeaderImage(newAccount.getBackground());
                    // si fa tornare il contenuto della cover visibile (ma l'utente non nota nulla)
                    ((View) usercover).setAlpha(1);

                    // switch numbers
                    currentAccount.setAccountNumber(newAccount.getAccountNumber());
                    newAccount.setAccountNumber(MaterialAccount.FIRST_ACCOUNT);

                    // change pointer to newAccount
                    currentAccount = newAccount;

                    // si chiude il drawer
                    if (!deviceSupportMultiPane())
                        layout.closeDrawer(drawer);

                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // se si annulla l'animazione si conclude e basta.
                    onAnimationEnd(animation);
                }
            });

            set.start();
        } else {
            // for minor version no animation is used.
            // switch numbers
            currentAccount.setAccountNumber(newAccount.getAccountNumber());
            newAccount.setAccountNumber(MaterialAccount.FIRST_ACCOUNT);
            // change pointer to newAccount
            currentAccount = newAccount;
            // refresh views
            notifyAccountDataChanged();

            if (!deviceSupportMultiPane())
                layout.closeDrawer(drawer);
        }
    }

    private int getHeight() {
        int height = 0;
        Display display = getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height = display.getHeight();  // deprecated
        }
        return height;
    }

    private boolean deviceSupportMultiPane() {
        if (multiPaneSupport && resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && resources.getConfiguration().smallestScreenWidthDp >= 600)
            return true;
        else
            return false;
    }

    protected void setSectionsTouch(boolean isTouchable) {
        for (MaterialSection section : sectionList) {
            section.setTouchable(isTouchable);
        }
        for (MaterialSection section : bottomSectionList) {
            section.setTouchable(isTouchable);
        }
    }

    private void recycleAccounts() {
        for (MaterialAccount account : accountManager) {
            account.recycle();
        }
    }

    protected int darkenColor(int color) {
        if (color == primaryColor)
            return primaryDarkColor;

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // value component
        return Color.HSVToColor(hsv);
    }

    @Override
    public void onClick(MaterialSection section) {

        switch (section.getTarget()) {
            case MaterialSection.TARGET_FRAGMENT:

                setFragment((Fragment) section.getTargetFragment(), section.getTitle(), (Fragment) currentSection.getTargetFragment());
                changeToolbarColor(section);

                // remove the last child from the stack
                if (!isCurrentFragmentChild) {
                    childFragmentStack.remove(childFragmentStack.size() - 1);
                    childTitleStack.remove(childTitleStack.size() - 1);
                } else
                    for (int i = childFragmentStack.size() - 1; i >= 0; i--) { // if a section is clicked when user is into a child remove all childs from stack
                        childFragmentStack.remove(i);
                        childTitleStack.remove(i);
                    }

                // add to the childStack the Fragment and title
                childFragmentStack.add((Fragment) section.getTargetFragment());
                childTitleStack.add(section.getTitle());
                isCurrentFragmentChild = false;
                pulsante.setDrawerIndicatorEnabled(true);

                if (section.getPosition() == 0 && flag == true) {
                    globalView.setVisibility(View.VISIBLE);

                } else {

                    globalView.setVisibility(View.GONE);
                }

                break;
            case MaterialSection.TARGET_ACTIVITY:
                this.startActivity(section.getTargetIntent());
                if (!deviceSupportMultiPane())
                    layout.closeDrawer(drawer);
                break;
            // TARGET_LISTENER viene gestito internamente
            case MaterialSection.TARGET_LISTENER:
                if (!deviceSupportMultiPane())
                    layout.closeDrawer(drawer);
            default:
                break;
        }
        currentSection = section;

        int position = section.getPosition();

        for (MaterialSection mySection : sectionList) {
            if (position != mySection.getPosition())
                mySection.unSelect();
        }
        for (MaterialSection mySection : bottomSectionList) {
            if (position != mySection.getPosition())
                mySection.unSelect();
        }

        if (!deviceSupportMultiPane()) {
            drawerTouchLocked = true;
            setSectionsTouch(!drawerTouchLocked);
        }
    }

    @Override
    public void onUserPhotoLoaded(MaterialAccount account) {
        if (account.getAccountNumber() <= MaterialAccount.THIRD_ACCOUNT)
            notifyAccountDataChanged();
    }

    @Override
    public void onBackgroundLoaded(MaterialAccount account) {
        if (account.getAccountNumber() <= MaterialAccount.THIRD_ACCOUNT)
            notifyAccountDataChanged();
    }

    // method used for change supports
    public void setAccountListener(MaterialAccountListener listener) {
        this.accountListener = listener;
    }

    public void setDrawerListener(DrawerLayout.DrawerListener listener) {
        this.drawerListener = listener;
    }

    public void addMultiPaneSupport() {
        this.multiPaneSupport = true;
    }

    public void allowArrowAnimation() {
        slidingDrawerEffect = true;
    }

    public void disableLearningPattern() {
        learningPattern = false;
    }

    /**
     * Set the HomeAsUpIndicator that is visible when user navigate to a fragment child
     * <p/>
     * N.B. call this method AFTER the init() to leave the time to instantiate the ActionBarDrawerToggle
     *
     * @param resId the id to resource drawable to use as indicator
     * @return true if indicator is setted, false otherwise
     */
    public boolean setHomeAsUpIndicator(int resId) {
        return setHomeAsUpIndicator(resources.getDrawable(resId));
    }

    /**
     * Set the HomeAsUpIndicator that is visible when user navigate to a fragment child
     *
     * @param indicator the resource drawable to use as indicator
     * @return true if indicator is setted, false otherwise
     */
    public boolean setHomeAsUpIndicator(Drawable indicator) {
        if (pulsante != null) { // if multipane support is enabled and device is a tablet this call do nothing
            pulsante.setHomeAsUpIndicator(indicator);
            return true;
        } else return false;
    }

    public void changeToolbarColor(MaterialSection section) {

        int sectionPrimaryColor;
        int sectionPrimaryColorDark;

        if (section.hasSectionColor() && !uniqueToolbarColor) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                if (!section.hasSectionColorDark())
                    sectionPrimaryColorDark = darkenColor(section.getSectionColor());
                else
                    sectionPrimaryColorDark = section.getSectionColorDark();
            } else
                sectionPrimaryColorDark = section.getSectionColor(); // Lollipop have his darker status bar

            sectionPrimaryColor = section.getSectionColor();
        } else {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
                sectionPrimaryColorDark = primaryDarkColor;
            else
                sectionPrimaryColorDark = primaryColor; // Lollipop have his darker status bar | under kitkat is not showed.

            sectionPrimaryColor = primaryColor;
        }

        this.getToolbar().setBackgroundColor(sectionPrimaryColor);
        this.statusBar.setImageDrawable(new ColorDrawable(sectionPrimaryColorDark));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.getWindow().setStatusBarColor(sectionPrimaryColorDark);
    }

    public void changeToolbarColor(int primaryColor, int primaryDarkColor) {
        if (statusBar != null)
            this.statusBar.setImageDrawable(new ColorDrawable(primaryDarkColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.getWindow().setStatusBarColor(primaryDarkColor);

        if (getToolbar() != null)
            this.getToolbar().setBackgroundColor(primaryColor);
    }

    public void setBackPattern(int backPattern) {
        this.backPattern = backPattern;
    }

    public void setDrawerHeaderCustom(View view) {
        if (drawerHeaderType != DRAWERHEADER_CUSTOM)
            throw new RuntimeException("Your header is not setted to Custom, check in your styles.xml");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        customDrawerHeader.addView(view, params);

    }

    public void setDrawerHeaderImage(Bitmap background) {
        switch (drawerHeaderType) {
            case DRAWERHEADER_ACCOUNTS:
                usercover.setImageBitmap(background);
                break;
            case DRAWERHEADER_IMAGE:
                ImageView image = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                image.setImageBitmap(background);

                customDrawerHeader.addView(image, params);
                break;
            default:
                throw new RuntimeException("Your drawer configuration don't support a background image, check in your styles.xml");
        }

    }

    public void setDrawerHeaderImage(int backgroundId) {
        setDrawerHeaderImage(resources.getDrawable(backgroundId));
    }

    public void setDrawerHeaderImage(Drawable background) {
        switch (drawerHeaderType) {
            case DRAWERHEADER_IMAGE:
                ImageView image = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                image.setImageDrawable(background);

                customDrawerHeader.addView(image, params);
                break;
            case DRAWERHEADER_ACCOUNTS:
                usercover.setImageDrawable(background);
                break;
            default:
                throw new RuntimeException("Your drawer configuration don't support a background image, check in your styles.xml");
        }
    }

    // Method used for customize layout

    public void setUserEmail(String email) {
        usermail.setText(email);
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public void setFirstAccountPhoto(Drawable photo) {
        userphoto.setImageDrawable(photo);
    }

    public void setSecondAccountPhoto(Drawable photo) {
        userSecondPhoto.setImageDrawable(photo);
    }

    public void setThirdAccountPhoto(Drawable photo) {
        userThirdPhoto.setImageDrawable(photo);
    }

    public void setDrawerBackgroundColor(int color) {
        drawer.setBackgroundColor(color);
    }

    public void addSection(MaterialSection section) {
        section.setPosition(sectionList.size());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (136*1.0/1080* DeviceInfoUtil.getScreenHeight(this)));
        section.setTypeface(fontManager.getRobotoMedium());
        sectionList.add(section);
        sections.addView(section.getView(), params);

        // add the element to the list
        elementsList.add(ELEMENT_TYPE_SECTION);
    }

    public void addBottomSection(MaterialSection section) {
        section.setPosition(BOTTOM_SECTION_START + bottomSectionList.size());
        section.setTypeface(fontManager.getRobotoRegular());
        bottomSectionList.add(section);

        // add the element to the list
        elementsList.add(ELEMENT_TYPE_BOTTOM_SECTION);
    }

    public void addAccountSection(MaterialSection section) {
        section.setPosition(accountSectionList.size());
        section.setTypeface(fontManager.getRobotoMedium());
        accountSectionList.add(section);
    }

    public void addDivisor() {
        View view = new View(this);
        view.setBackgroundColor(Color.parseColor("#e0e0e0"));
        // height 1 px
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(0, (int) (8 * density), 0, (int) (8 * density));

        sections.addView(view, params);

        // add the element to the list
        elementsList.add(ELEMENT_TYPE_DIVISOR);
    }

    public void addSubheader(CharSequence title) {
        MaterialSubheader subheader = new MaterialSubheader(this);
        subheader.setTitle(title);
        subheader.setTitleFont(fontManager.getRobotoRegular());


        subheaderList.add(subheader);
        sections.addView(subheader.getView());

        // add the element to the list
        elementsList.add(ELEMENT_TYPE_SUBHEADER);
    }

    public void addAccount(MaterialAccount account) {
        if (DRAWERHEADER_ACCOUNTS != drawerHeaderType) {
            throw new RuntimeException("Your header is not setted to Accounts, check in your styles.xml");
        }

        account.setAccountListener(this);
        account.setAccountNumber(accountManager.size());
        accountManager.add(account);
    }

    /**
     * Reload Application data from Account Information
     */
    public void notifyAccountDataChanged() {
        switch (accountManager.size()) {
            case 3:
                this.setThirdAccountPhoto(findAccountNumber(MaterialAccount.THIRD_ACCOUNT).getCircularPhoto());
            case 2:
                this.setSecondAccountPhoto(findAccountNumber(MaterialAccount.SECOND_ACCOUNT).getCircularPhoto());
            case 1:
                this.setFirstAccountPhoto(currentAccount.getCircularPhoto());
                this.setDrawerHeaderImage(currentAccount.getBackground());
                this.setUsername(currentAccount.getTitle());
                this.setUserEmail(currentAccount.getSubTitle());
            default:
        }
    }

    // create sections

    public MaterialSection newSection(String title, Drawable icon, Fragment target) {
        MaterialSection section = new MaterialSection<Fragment>(this, MaterialSection.ICON_24DP, rippleSupport, MaterialSection.TARGET_FRAGMENT);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    public MaterialSection newSection(String title, Drawable icon, Intent target) {
        MaterialSection section = new MaterialSection<Fragment>(this, MaterialSection.ICON_24DP, rippleSupport, MaterialSection.TARGET_ACTIVITY);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    public MaterialSection newSection(String title, Drawable icon, MaterialSectionListener target) {
        MaterialSection section = new MaterialSection<Fragment>(this, MaterialSection.ICON_24DP, rippleSupport, MaterialSection.TARGET_LISTENER);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    public MaterialSection newSection(String title, Bitmap icon, Fragment target) {
        MaterialSection section = new MaterialSection<Fragment>(this, MaterialSection.ICON_24DP, rippleSupport, MaterialSection.TARGET_FRAGMENT);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    public MaterialSection newSection(String title, Bitmap icon, Intent target) {
        MaterialSection section = new MaterialSection<Fragment>(this, MaterialSection.ICON_24DP, rippleSupport, MaterialSection.TARGET_ACTIVITY);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    public MaterialSection newSection(String title, Bitmap icon, MaterialSectionListener target) {
        MaterialSection section = new MaterialSection<Fragment>(this, MaterialSection.ICON_24DP, rippleSupport, MaterialSection.TARGET_LISTENER);
        section.setOnClickListener(this);
        section.setIcon(icon);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    public MaterialSection newSection(String title, int icon, Fragment target) {
        return newSection(title, resources.getDrawable(icon), target);
    }

    public MaterialSection newSection(String title, int icon, Intent target) {
        return newSection(title, resources.getDrawable(icon), target);
    }

    public MaterialSection newSection(String title, int icon, MaterialSectionListener target) {
        return newSection(title, resources.getDrawable(icon), target);
    }

    @SuppressWarnings("unchecked")
    public MaterialSection newSection(String title, Fragment target) {
        MaterialSection section = new MaterialSection<Fragment>(this, MaterialSection.ICON_NO_ICON, rippleSupport, MaterialSection.TARGET_FRAGMENT);
        section.setOnClickListener(this);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    @SuppressWarnings("unchecked")
    public MaterialSection newSection(String title, Intent target) {
        MaterialSection section = new MaterialSection<Fragment>(this, MaterialSection.ICON_NO_ICON, rippleSupport, MaterialSection.TARGET_ACTIVITY);
        section.setOnClickListener(this);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    @SuppressWarnings("unchecked")
    public MaterialSection newSection(String title, MaterialSectionListener target) {
        MaterialSection section = new MaterialSection<Fragment>(this, MaterialSection.ICON_NO_ICON, rippleSupport, MaterialSection.TARGET_LISTENER);
        section.setOnClickListener(this);
        section.setTitle(title);
        section.setTarget(target);

        return section;
    }

    // abstract methods

    public abstract void init(Bundle savedInstanceState);

    public void setProfileListener(View.OnClickListener listener){
        this.currentAccountListener = listener;
    };

    public void onHomeAsUpSelected() {

    }

    // get methods

    public Toolbar getToolbar() {
        return toolbar;
    }

    public MaterialSection getCurrentSection() {
        return currentSection;
    }

    public MaterialAccount getCurrentAccount() {
        return currentAccount;
    }

    public MaterialAccount getAccountAtCurrentPosition(int position) {

        if (position < 0 || position >= accountManager.size())
            throw new RuntimeException("Account Index Overflow");

        return findAccountNumber(position);
    }
}