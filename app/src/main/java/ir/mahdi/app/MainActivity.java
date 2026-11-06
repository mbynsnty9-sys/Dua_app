package ir.mahdi.app;

import android.app.*;
import android.os.*;
import android.content.*;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import android.media.MediaPlayer;
import java.io.*;
import java.util.*;

public class MainActivity extends Activity {
  final int forest=0xff123b32, forest2=0xff1d5949, gold=0xffc5a36a, cream=0xfff7f3e9, ink=0xff35352f, nightBg=0xff0b211c, nightSurface=0xff14352c, nightText=0xfff5f3eb;
  boolean nightMode=true, introRunning=false;
  final int PICK_AUDIO=10, PICK_ARABIC_FONT=11, PICK_PERSIAN_FONT=12;
  LinearLayout page;
  MediaPlayer player;
  Typeface arabicFont=Typeface.create("serif",0), persianFont=Typeface.create("sans-serif",0);
  String defaultAudio="";
  int selected=-1;
  boolean auto=false;
  Handler handler=new Handler();
  final String[] titles={"دعای فرج","دعای عهد"};
  final String[] subtitles={"اللهم کن لولیک الحجة بن الحسن","تجدید پیمان با امام زمان (عج)"};

  final String[][] ar={
    {
      "اللَّهُمَّ كُنْ لِوَلِيِّكَ الْحُجَّةِ بْنِ الْحَسَنِ، صَلَوَاتُكَ عَلَيْهِ وَعَلَى آبَائِهِ،",
      "فِي هَذِهِ السَّاعَةِ وَفِي كُلِّ سَاعَةٍ، وَلِيًّا وَحَافِظًا، وَقَائِدًا وَنَاصِرًا، وَدَلِيلًا وَعَيْنًا،",
      "حَتَّى تُسْكِنَهُ أَرْضَكَ طَوْعًا، وَتُمَتِّعَهُ فِيهَا طَوِيلًا."
    },
    {
      "اللَّهُمَّ رَبَّ النُّورِ الْعَظِيمِ، وَرَبَّ الْكُرْسِيِّ الرَّفِيعِ، وَرَبَّ الْبَحْرِ الْمَسْجُورِ، وَمُنْزِلَ التَّوْرَاةِ وَالْإِنْجِيلِ وَالزَّبُورِ، وَرَبَّ الظِّلِّ وَالْحَرُورِ، وَمُنْزِلَ الْقُرْآنِ الْعَظِيمِ، وَرَبَّ الْمَلَائِكَةِ الْمُقَرَّبِينَ، وَالْأَنْبِيَاءِ وَالْمُرْسَلِينَ.",
      "اللَّهُمَّ إِنِّي أَسْأَلُكَ بِوَجْهِكَ الْكَرِيمِ، وَبِنُورِ وَجْهِكَ الْمُنِيرِ، وَمُلْكِكَ الْقَدِيمِ، يَا حَيُّ يَا قَيُّومُ، أَسْأَلُكَ بِاسْمِكَ الَّذِي أَشْرَقَتْ بِهِ السَّمَاوَاتُ وَالْأَرَضُونَ، وَبِاسْمِكَ الَّذِي يَصْلُحُ بِهِ الْأَوَّلُونَ وَالْآخِرُونَ، يَا حَيًّا قَبْلَ كُلِّ حَيٍّ، وَيَا حَيًّا بَعْدَ كُلِّ حَيٍّ، وَيَا حَيًّا حِينَ لَا حَيَّ، يَا مُحْيِيَ الْمَوْتَى وَمُمِيتَ الْأَحْيَاءِ، يَا حَيُّ لَا إِلَهَ إِلَّا أَنْتَ.",
      "اللَّهُمَّ بَلِّغْ مَوْلَانَا الْإِمَامَ الْهَادِيَ الْمَهْدِيَّ الْقَائِمَ بِأَمْرِكَ، صَلَوَاتُ اللَّهِ عَلَيْهِ وَعَلَى آبَائِهِ الطَّاهِرِينَ، عَنْ جَمِيعِ الْمُؤْمِنِينَ وَالْمُؤْمِنَاتِ فِي مَشَارِقِ الْأَرْضِ وَمَغَارِبِهَا، سَهْلِهَا وَجَبَلِهَا، وَبَرِّهَا وَبَحْرِهَا، وَعَنِّي وَعَنْ وَالِدَيَّ مِنَ الصَّلَوَاتِ زِنَةَ عَرْشِ اللَّهِ وَمِدَادَ كَلِمَاتِهِ، وَمَا أَحْصَاهُ عِلْمُهُ وَأَحَاطَ بِهِ كِتَابُهُ.",
      "اللَّهُمَّ إِنِّي أُجَدِّدُ لَهُ فِي صَبِيحَةِ يَوْمِي هَذَا وَمَا عِشْتُ مِنْ أَيَّامِي عَهْدًا وَعَقْدًا وَبَيْعَةً لَهُ فِي عُنُقِي، لَا أَحُولُ عَنْهَا وَلَا أَزُولُ أَبَدًا. اللَّهُمَّ اجْعَلْنِي مِنْ أَنْصَارِهِ وَأَعْوَانِهِ، وَالذَّابِّينَ عَنْهُ، وَالْمُسَارِعِينَ إِلَيْهِ فِي قَضَاءِ حَوَائِجِهِ، وَالْمُمْتَثِلِينَ لِأَوَامِرِهِ، وَالْمُحَامِينَ عَنْهُ، وَالسَّابِقِينَ إِلَى إِرَادَتِهِ، وَالْمُسْتَشْهَدِينَ بَيْنَ يَدَيْهِ.",
      "اللَّهُمَّ إِنْ حَالَ بَيْنِي وَبَيْنَهُ الْمَوْتُ الَّذِي جَعَلْتَهُ عَلَى عِبَادِكَ حَتْمًا مَقْضِيًّا، فَأَخْرِجْنِي مِنْ قَبْرِي مُؤْتَزِرًا كَفَنِي، شَاهِرًا سَيْفِي، مُجَرِّدًا قَنَاتِي، مُلَبِّيًا دَعْوَةَ الدَّاعِي فِي الْحَاضِرِ وَالْبَادِي.",
      "اللَّهُمَّ أَرِنِي الطَّلْعَةَ الرَّشِيدَةَ، وَالْغُرَّةَ الْحَمِيدَةَ، وَاكْحُلْ نَاظِرِي بِنَظْرَةٍ مِنِّي إِلَيْهِ، وَعَجِّلْ فَرَجَهُ، وَسَهِّلْ مَخْرَجَهُ، وَأَوْسِعْ مَنْهَجَهُ، وَاسْلُكْ بِي مَحَجَّتَهُ، وَأَنْفِذْ أَمْرَهُ، وَاشْدُدْ أَزْرَهُ، وَاعْمُرِ اللَّهُمَّ بِهِ بِلَادَكَ، وَأَحْيِ بِهِ عِبَادَكَ.",
      "فَإِنَّكَ قُلْتَ وَقَوْلُكَ الْحَقُّ: ظَهَرَ الْفَسَادُ فِي الْبَرِّ وَالْبَحْرِ بِمَا كَسَبَتْ أَيْدِي النَّاسِ، فَأَظْهِرِ اللَّهُمَّ لَنَا وَلِيَّكَ وَابْنَ بِنْتِ نَبِيِّكَ، الْمُسَمَّى بِاسْمِ رَسُولِكَ، حَتَّى لَا يَظْفَرَ بِشَيْءٍ مِنَ الْبَاطِلِ إِلَّا مَزَّقَهُ، وَيُحِقَّ الْحَقَّ وَيُحَقِّقَهُ.",
      "وَاجْعَلْهُ اللَّهُمَّ مَفْزَعًا لِمَظْلُومِ عِبَادِكَ، وَنَاصِرًا لِمَنْ لَا يَجِدُ لَهُ نَاصِرًا غَيْرَكَ، وَمُجَدِّدًا لِمَا عُطِّلَ مِنْ أَحْكَامِ كِتَابِكَ، وَمُشَيِّدًا لِمَا وَرَدَ مِنْ أَعْلَامِ دِينِكَ وَسُنَنِ نَبِيِّكَ صَلَّى اللَّهُ عَلَيْهِ وَآلِهِ، وَاجْعَلْهُ اللَّهُمَّ مِمَّنْ حَصَّنْتَهُ مِنْ بَأْسِ الْمُعْتَدِينَ.",
      "اللَّهُمَّ وَسُرَّ نَبِيَّكَ مُحَمَّدًا صَلَّى اللَّهُ عَلَيْهِ وَآلِهِ بِرُؤْيَتِهِ، وَمَنْ تَبِعَهُ عَلَى دَعْوَتِهِ، وَارْحَمِ اسْتِكَانَتَنَا بَعْدَهُ. اللَّهُمَّ اكْشِفْ هَذِهِ الْغُمَّةَ عَنْ هَذِهِ الْأُمَّةِ بِحُضُورِهِ، وَعَجِّلْ لَنَا ظُهُورَهُ، إِنَّهُمْ يَرَوْنَهُ بَعِيدًا وَنَرَاهُ قَرِيبًا، بِرَحْمَتِكَ يَا أَرْحَمَ الرَّاحِمِينَ. الْعَجَلَ الْعَجَلَ يَا مَوْلَايَ يَا صَاحِبَ الزَّمَانِ."
    }
  };

  final String[][] fa={
    {
      "خدایا، برای ولیّ خود، حجّت بن حسن ـ که درودهای تو بر او و پدرانش باد ـ باش؛",
      "در این ساعت و در هر ساعت، سرپرست و نگهبان، پیشوا و یاور، راهنما و دیده‌بان او باش؛",
      "تا او را با خشنودی در زمین خود جای دهی و زمانی طولانی در آن بهره‌مند سازی."
    },
    {
      "خدایا، ای پروردگار نور بزرگ، پروردگار کرسی بلندمرتبه و دریای آکنده؛ ای فرستندهٔ تورات و انجیل و زبور، پروردگار سایه و گرمای آفتاب، فرستندهٔ قرآن بزرگ و پروردگار فرشتگان مقرّب و پیامبران و فرستادگان.",
      "خدایا، از تو به حق ذات کریمت، نور روشنی‌بخش چهره‌ات و فرمانروایی دیرینه‌ات درخواست می‌کنم. ای زنده و برپادارندهٔ هستی! از تو به حق نامی درخواست می‌کنم که آسمان‌ها و زمین‌ها به آن روشن شدند و به حق نامی که پیشینیان و پسینیان به برکت آن سامان می‌یابند. ای زنده پیش از هر زنده، ای زنده پس از هر زنده و ای زنده در آن هنگام که هیچ زنده‌ای نبود؛ ای زنده‌کنندهٔ مردگان و میرانندهٔ زندگان؛ ای زنده‌ای که معبودی جز تو نیست.",
      "خدایا، درودهایی به مولای ما، امام هدایتگر و مهدیِ قیام‌کننده به فرمانت برسان؛ درود خدا بر او و پدران پاکش باد. این درودها را از سوی همهٔ مردان و زنان باایمان در خاور و باختر زمین، دشت و کوه، خشکی و دریا، و نیز از سوی من و پدر و مادرم قرار ده؛ درودی به وزن عرش خدا و به شمار کلمات او، به اندازهٔ آنچه دانشش احصا کرده و کتابش بر آن احاطه دارد.",
      "خدایا، در بامداد امروز و در تمام روزهایی که زنده‌ام، پیمان و عهد و بیعت خود را با او تازه می‌کنم؛ پیمانی که بر عهدهٔ من است و هرگز از آن روی نمی‌گردانم و دست برنمی‌دارم. خدایا، مرا از یاوران و پشتیبانان او، دفاع‌کنندگان از او، شتابندگان در برآوردن نیازهایش، فرمان‌برداران اوامر او، حمایت‌کنندگان از او، پیشگامان در انجام خواسته‌اش و آنان که در پیشگاهش به شهادت می‌رسند قرار ده.",
      "خدایا، اگر مرگی که آن را بر بندگانت حتمی و قطعی قرار داده‌ای میان من و او فاصله انداخت، مرا از قبرم بیرون آور؛ در حالی که کفنم را بر کمر بسته‌ام، شمشیرم آخته و نیزه‌ام آماده است و دعوت آن دعوت‌کننده را در شهر و بیابان لبیک می‌گویم.",
      "خدایا، آن چهرهٔ هدایتگر و پیشانی ستوده را به من بنمایان و دیدگانم را با نگاهی به سوی او روشن کن. در فرجش شتاب فرما، راه ظهورش را آسان و مسیرش را گسترده ساز، مرا در راه روشن او قرار ده، فرمانش را نافذ گردان و پشتش را استوار ساز؛ خدایا، سرزمین‌هایت را به دست او آباد و بندگانت را به وسیلهٔ او زنده کن.",
      "زیرا تو گفته‌ای و سخنت حق است: «به سبب آنچه دست‌های مردم فراهم آورده، فساد در خشکی و دریا آشکار شده است». پس خدایا، ولیّ خود و فرزند دختر پیامبرت را که هم‌نام فرستادهٔ توست برای ما آشکار ساز؛ تا با هیچ باطلی روبه‌رو نشود مگر آنکه آن را درهم بشکند و حق را استوار و محقق گرداند.",
      "خدایا، او را پناه مظلومان از بندگانت و یاور کسی قرار ده که جز تو یاوری نمی‌یابد؛ احیاگر احکامی از کتابت قرار ده که تعطیل شده‌اند و استوارکنندهٔ نشانه‌های دین و سنت‌های پیامبرت؛ و او را از کسانی قرار ده که از گزند تجاوزگران در پناه و حفاظت تو هستند.",
      "خدایا، پیامبرت محمد ـ درود خدا بر او و خاندانش باد ـ را با دیدار او و پیروان دعوتش شادمان ساز و بر ناتوانی و فروتنی ما پس از او رحم کن. خدایا، این اندوه را با حضور او از این امت برطرف ساز و در ظهورش برای ما شتاب فرما؛ آنان ظهورش را دور می‌بینند و ما آن را نزدیک می‌دانیم. به رحمتت، ای مهربان‌ترین مهربانان. شتاب کن، شتاب کن، ای مولای من، ای صاحب زمان."
    }
  };

  @Override public void onCreate(Bundle b){
    super.onCreate(b);
    loadPrefs();
    nightMode=getSharedPreferences("prefs",0).getBoolean("night",true);
    showWelcome();
  }
  int dp(float n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
  GradientDrawable bg(int c,int r,int stroke){GradientDrawable d=new GradientDrawable();d.setColor(c);d.setCornerRadius(dp(r));if(stroke!=0)d.setStroke(dp(1),stroke);return d;}
  TextView tx(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setGravity(Gravity.CENTER_VERTICAL);t.setTextDirection(View.TEXT_DIRECTION_RTL);t.setPadding(dp(14),dp(10),dp(14),dp(10));if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
  LinearLayout col(){LinearLayout l=new LinearLayout(this);l.setOrientation(1);return l;}
  LinearLayout.LayoutParams m(int top,int bottom){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(dp(16),dp(top),dp(16),dp(bottom));return p;}
  TextView btn(String s){TextView t=tx(s,14,Color.WHITE,true);t.setGravity(Gravity.CENTER);t.setBackground(bg(forest2,18,0));return t;}
  void loadPrefs(){android.content.SharedPreferences p=getSharedPreferences("prefs",0);defaultAudio=p.getString("audio","");try{String a=p.getString("arabic_font",""),f=p.getString("persian_font","");if(!a.isEmpty())arabicFont=Typeface.createFromFile(a);if(!f.isEmpty())persianFont=Typeface.createFromFile(f);}catch(Exception ignored){}}
  void showWelcome(){
    introRunning=true;
    getWindow().setStatusBarColor(nightBg);
    getWindow().setNavigationBarColor(nightBg);
    FrameLayout root=new FrameLayout(this);
    root.setBackgroundColor(nightBg);
    WelcomeArt art=new WelcomeArt(this);
    root.addView(art,new FrameLayout.LayoutParams(-1,-1));
    TextView welcome=tx("خوش آمدید",30,Color.WHITE,true);
    welcome.setGravity(Gravity.CENTER);
    welcome.setShadowLayer(dp(18),0,0,0xffffe6a3);
    welcome.setAlpha(0f);
    FrameLayout.LayoutParams wp=new FrameLayout.LayoutParams(-1,dp(90),Gravity.BOTTOM);
    wp.setMargins(dp(18),0,dp(18),dp(110));
    root.addView(welcome,wp);
    setContentView(root);
    welcome.animate().alpha(1f).setStartDelay(850).setDuration(1100).start();
    root.postDelayed(()->{if(introRunning){introRunning=false;showHome();}},3400);
  }
  int bgColor(){return nightMode?nightBg:cream;}
  int textColor(){return nightMode?nightText:ink;}
  int secondaryText(){return nightMode?0xffd0d8d1:0xff66665d;}
  void applyBars(){getWindow().setStatusBarColor(nightMode?nightBg:cream);getWindow().setNavigationBarColor(nightMode?nightBg:cream);if(Build.VERSION.SDK_INT>=23)getWindow().getDecorView().setSystemUiVisibility(nightMode?0:View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);}
  void toggleTheme(){nightMode=!nightMode;getSharedPreferences("prefs",0).edit().putBoolean("night",nightMode).apply();if(selected>=0)read(selected);else showHome();}
  void showHome(){
    auto=false;stopAudio();applyBars();page=col();page.setBackgroundColor(bgColor());
    FrameLayout hero=new FrameLayout(this);hero.setBackground(bg(nightMode?0xff102d25:0xffe8dfc8,0,0));
    hero.addView(new AmbientArt(this),new FrameLayout.LayoutParams(-1,dp(190)));
    LinearLayout title=col();title.setPadding(dp(18),dp(18),dp(18),dp(18));
    TextView mini=tx("کتابخانهٔ دعا و مناجات",13,0xffe6d5ae,true),big=tx("قرارِ دل",30,Color.WHITE,true),desc=tx("آرامش در یاد و نیایش",13,0xffe7e9df,false);
    title.addView(mini);title.addView(big);title.addView(desc);
    hero.addView(title,new FrameLayout.LayoutParams(-1,-2,Gravity.BOTTOM|Gravity.RIGHT));page.addView(hero);
    LinearLayout top=new LinearLayout(this);top.setGravity(Gravity.CENTER_VERTICAL);top.setPadding(dp(12),dp(8),dp(12),dp(4));
    TextView welcome=tx("خوش آمدید",21,textColor(),true);top.addView(welcome,new LinearLayout.LayoutParams(0,-2,1));
    TextView theme=tx(nightMode?"☀ حالت روز":"☾ حالت شب",13,gold,true);theme.setGravity(Gravity.CENTER);theme.setBackground(bg(nightMode?nightSurface:0xffeee5d3,22,gold));top.addView(theme,new LinearLayout.LayoutParams(-2,dp(42)));theme.setOnClickListener(v->toggleTheme());page.addView(top);
    TextView hint=tx("برای شروع، یک دعا را انتخاب کنید",14,secondaryText(),false);hint.setPadding(dp(16),dp(14),dp(16),dp(8));page.addView(hint);
    addCard(0,"دعای فرج","اللهم کن لولیک الحجة بن الحسن",0);
    addCard(1,"دعای عهد","تجدید پیمان با امام زمان (عج)",1);
    TextView settings=tx("⚙   تنظیمات، فونت‌ها و صوت",15,gold,true);settings.setGravity(Gravity.CENTER);settings.setPadding(dp(14),dp(18),dp(14),dp(18));page.addView(settings,m(12,18));settings.setOnClickListener(v->settings());
    TextView foot=tx("قرارِ دل · نسخهٔ آزمایشی",11,secondaryText(),false);foot.setGravity(Gravity.CENTER);page.addView(foot);
    ScrollView sc=new ScrollView(this);sc.setFillViewport(true);sc.addView(page);setContentView(sc);
  }
  void addCard(int index,String title,String desc,int tint){
    LinearLayout row=new LinearLayout(this);row.setOrientation(0);row.setGravity(Gravity.CENTER_VERTICAL);row.setPadding(dp(10),dp(14),dp(10),dp(14));
    DigitalIcon icon=new DigitalIcon(this,index);row.addView(icon,new LinearLayout.LayoutParams(dp(66),dp(66)));
    LinearLayout words=col();TextView h=tx(title,19,textColor(),true),sub=tx(desc,12,secondaryText(),false);words.addView(h);words.addView(sub);
    LinearLayout.LayoutParams wordsLp=new LinearLayout.LayoutParams(0,-2,1);wordsLp.setMargins(dp(10),0,dp(4),0);row.addView(words,wordsLp);
    TextView arrow=tx("›",30,gold,true);arrow.setGravity(Gravity.CENTER);row.addView(arrow,new LinearLayout.LayoutParams(dp(28),dp(48)));
    View line=new View(this);line.setBackgroundColor(nightMode?0xff29463d:0xffded5c3);
    LinearLayout wrap=col();wrap.addView(row,new LinearLayout.LayoutParams(-1,-2));wrap.addView(line,new LinearLayout.LayoutParams(-1,dp(1)));
    wrap.setOnClickListener(v->read(index));page.addView(wrap,new LinearLayout.LayoutParams(-1,-2));
  }
  void read(int index){
    selected=index;auto=false;stopAudio();applyBars();page=col();page.setBackgroundColor(bgColor());
    TextView back=tx("‹   بازگشت",15,gold,true);back.setPadding(dp(14),dp(12),dp(14),dp(12));page.addView(back);back.setOnClickListener(v->showHome());
    TextView heading=tx(titles[index],25,textColor(),true);heading.setPadding(dp(12),dp(6),dp(12),dp(10));page.addView(heading);
    TextView sub=tx(subtitles[index],13,secondaryText(),false);sub.setPadding(dp(12),0,dp(12),dp(12));page.addView(sub);
    ScrollView scroll=new ScrollView(this);LinearLayout body=col();body.setPadding(dp(10),dp(4),dp(10),dp(12));
    for(int i=0;i<ar[index].length;i++){
      TextView a=tx(ar[index][i],25,textColor(),true);a.setTypeface(arabicFont,Typeface.BOLD);a.setGravity(Gravity.CENTER);a.setLineSpacing(dp(10),1.22f);a.setPadding(dp(4),dp(22),dp(4),dp(18));a.setBackgroundColor(Color.TRANSPARENT);body.addView(a,new LinearLayout.LayoutParams(-1,-2));
      TextView tr=tx(fa[index][i],17,textColor(),false);tr.setTypeface(persianFont);tr.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);tr.setLineSpacing(dp(7),1.18f);tr.setPadding(dp(4),dp(8),dp(4),dp(18));body.addView(tr,new LinearLayout.LayoutParams(-1,-2));
      if(i<ar[index].length-1){View divider=new View(this);divider.setBackgroundColor(nightMode?0xff29463d:0xffded5c3);LinearLayout.LayoutParams dl=new LinearLayout.LayoutParams(dp(52),dp(1));dl.gravity=Gravity.CENTER;dl.topMargin=dp(4);dl.bottomMargin=dp(4);body.addView(divider,dl);}
    }
    scroll.addView(body);page.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
    LinearLayout controls=new LinearLayout(this);controls.setPadding(dp(10),dp(8),dp(10),dp(8));
    TextView play=btn("▶ پخش صوت"),autoBtn=btn("حرکت خودکار");
    controls.addView(play,new LinearLayout.LayoutParams(0,dp(48),1));View gap=new View(this);controls.addView(gap,new LinearLayout.LayoutParams(dp(8),1));controls.addView(autoBtn,new LinearLayout.LayoutParams(0,dp(48),1));page.addView(controls);
    play.setOnClickListener(v->play(index,play));autoBtn.setOnClickListener(v->{auto=!auto;autoBtn.setText(auto?"توقف حرکت":"حرکت خودکار");if(auto)scrollText(scroll,autoBtn);});
    setContentView(page);
  }
  void scrollText(ScrollView s,TextView b){handler.postDelayed(new Runnable(){public void run(){if(!auto)return;int max=s.getChildAt(0).getHeight()-s.getHeight();if(s.getScrollY()>=max){auto=false;b.setText("حرکت خودکار");return;}s.smoothScrollBy(0,dp(2));scrollText(s,b);}},80);}
  void settings(){
    String[] opts={"حالت روز / شب","انتخاب فونت عربی از گوشی","انتخاب فونت فارسی از گوشی","انتخاب صوت پیش‌فرض","پاک‌کردن صوت پیش‌فرض"};
    new AlertDialog.Builder(this).setTitle("تنظیمات قرار دل").setItems(opts,(d,w)->{
      if(w==0)toggleTheme();
      if(w==1)pick(PICK_ARABIC_FONT,"font/ttf");
      if(w==2)pick(PICK_PERSIAN_FONT,"font/ttf");
      if(w==3)pick(PICK_AUDIO,"audio/*");
      if(w==4){defaultAudio="";savePrefs();showHome();}
    }).show();
  }
  void pick(int code,String type){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.addCategory(Intent.CATEGORY_OPENABLE);i.setType(type);startActivityForResult(i,code);}
  @Override protected void onActivityResult(int r,int result,Intent data){super.onActivityResult(r,result,data);if(result!=RESULT_OK||data==null||data.getData()==null)return;try{Uri u=data.getData();String path=copy(u,"asset_"+System.currentTimeMillis());if(r==PICK_AUDIO){long size=size(u);if(size>30L*1024*1024){toast("حجم صوت باید حداکثر ۳۰ مگابایت باشد.");return;}defaultAudio=path;savePrefs();toast("صوت پیش‌فرض ثبت شد.");}else if(r==PICK_ARABIC_FONT){arabicFont=Typeface.createFromFile(path);getSharedPreferences("prefs",0).edit().putString("arabic_font",path).apply();toast("فونت عربی ثبت شد.");}else if(r==PICK_PERSIAN_FONT){persianFont=Typeface.createFromFile(path);getSharedPreferences("prefs",0).edit().putString("persian_font",path).apply();toast("فونت فارسی ثبت شد.");}if(selected>=0)read(selected);else showHome();}catch(Exception e){toast("فایل انتخاب‌شده قابل استفاده نیست.");}}
  String copy(Uri u,String name)throws Exception{File f=new File(getFilesDir(),name);try(InputStream in=getContentResolver().openInputStream(u);OutputStream out=new FileOutputStream(f)){byte[] b=new byte[8192];int n;while((n=in.read(b))>0)out.write(b,0,n);}return f.getAbsolutePath();}
  long size(Uri u){try(Cursor c=getContentResolver().query(u,null,null,null,null)){if(c!=null&&c.moveToFirst()){int i=c.getColumnIndex(OpenableColumns.SIZE);if(i>=0)return c.getLong(i);}}return 0;}
  void savePrefs(){getSharedPreferences("prefs",0).edit().putString("audio",defaultAudio).apply();}
  void play(int index,TextView b){if(defaultAudio.isEmpty()){toast("برای پخش، ابتدا صوت پیش‌فرض انتخاب کن.");return;}stopAudio();try{player=new MediaPlayer();player.setDataSource(defaultAudio);player.setOnPreparedListener(p->{p.start();b.setText("■ توقف");});player.setOnCompletionListener(p->{b.setText("▶ پخش صوت");stopAudio();});player.prepareAsync();b.setOnClickListener(v->{stopAudio();b.setText("▶ پخش صوت");});}catch(Exception e){toast("پخش این فایل ممکن نیست.");stopAudio();}}
  void stopAudio(){if(player!=null){try{if(player.isPlaying())player.stop();player.release();}catch(Exception ignored){}player=null;}}
  void toast(String s){Toast.makeText(this,s,Toast.LENGTH_LONG).show();}
  @Override public void onBackPressed(){auto=false;stopAudio();showHome();}
  @Override protected void onDestroy(){auto=false;stopAudio();super.onDestroy();}

  class AmbientArt extends View{
    Paint p=new Paint(3);long start=System.currentTimeMillis();
    AmbientArt(Context c){super(c);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
    protected void onDraw(Canvas c){super.onDraw(c);float w=getWidth(),h=getHeight();p.setShader(new LinearGradient(0,0,w,h,0xff102e29,0xff28604e,Shader.TileMode.CLAMP));c.drawRect(0,0,w,h,p);p.setShader(null);
      p.setColor(0x20ffffff);for(int i=0;i<5;i++){float x=(float)(w*(.15+.18*i)+Math.sin((System.currentTimeMillis()-start)/1200.0+i)*dp(9));float y=h*(.22f+.13f*(i%3));c.drawCircle(x,y,dp(4+i%2),p);}
      p.setColor(0xffd7bd86);c.drawCircle(w-dp(55),dp(55),dp(28),p);p.setColor(0xff1d5143);c.drawCircle(w-dp(42),dp(44),dp(26),p);
      p.setColor(0xffead7a9);Path star=new Path();star.moveTo(w-dp(94),dp(32));star.lineTo(w-dp(89),dp(45));star.lineTo(w-dp(76),dp(49));star.lineTo(w-dp(89),dp(53));star.lineTo(w-dp(94),dp(66));star.lineTo(w-dp(99),dp(53));star.lineTo(w-dp(112),dp(49));star.lineTo(w-dp(99),dp(45));star.close();c.drawPath(star,p);
      postInvalidateDelayed(60);
    }
  }
  class DigitalIcon extends View{
    Paint p=new Paint(3);int which;
    DigitalIcon(Context c,int n){super(c);which=n;setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
    protected void onDraw(Canvas c){
      float w=getWidth(),h=getHeight(),r=Math.min(w,h)*.43f,cx=w/2,cy=h/2;
      p.setShadowLayer(dp(8),0,dp(3),0x8877c7a3);
      p.setShader(new RadialGradient(cx-r*.25f,cy-r*.35f,r*1.55f,new int[]{0xff5c9e80,0xff1a5946,0xff09261f},null,Shader.TileMode.CLAMP));
      c.drawCircle(cx,cy,r,p);p.clearShadowLayer();p.setShader(null);
      p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dp(1.4f));p.setColor(0xffd8bd7c);c.drawCircle(cx,cy,r,p);p.setStyle(Paint.Style.FILL);
      p.setColor(0x66ffffff);c.drawArc(cx-r*.7f,cy-r*.7f,cx+r*.7f,cy+r*.7f,205,125,false,p);
      p.setColor(0xfff3dfaa);p.setStrokeWidth(dp(2.2f));p.setStrokeCap(Paint.Cap.ROUND);
      if(which==0){
        Path crescent=new Path();crescent.addCircle(cx,cy,r*.36f,Path.Direction.CW);c.drawPath(crescent,p);
        p.setColor(0xff1a5946);c.drawCircle(cx+r*.14f,cy-r*.10f,r*.29f,p);
        p.setColor(0xffffe6ad);Path star=new Path();star.moveTo(cx+r*.38f,cy-r*.35f);star.lineTo(cx+r*.43f,cy-r*.21f);star.lineTo(cx+r*.57f,cy-r*.17f);star.lineTo(cx+r*.43f,cy-r*.12f);star.lineTo(cx+r*.38f,cy+r*.02f);star.lineTo(cx+r*.33f,cy-r*.12f);star.lineTo(cx+r*.19f,cy-r*.17f);star.lineTo(cx+r*.33f,cy-r*.21f);star.close();c.drawPath(star,p);
      }else{
        p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dp(2.4f));c.drawRoundRect(cx-r*.38f,cy-r*.32f,cx+r*.38f,cy+r*.34f,dp(5),dp(5),p);p.setStyle(Paint.Style.FILL);
        for(int i=0;i<3;i++){p.setColor(0xffffe6ad);c.drawLine(cx-r*.23f,cy-r*.14f+i*dp(9),cx+r*.23f,cy-r*.14f+i*dp(9),p);}
        p.setColor(0xffffe6ad);c.drawCircle(cx+r*.25f,cy+r*.27f,dp(3),p);
      }
    }
  }
  class WelcomeArt extends View{
    Paint p=new Paint(3);long born=System.currentTimeMillis();
    WelcomeArt(Context c){super(c);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
    protected void onDraw(Canvas c){
      float w=getWidth(),h=getHeight(),t=Math.min(1f,(System.currentTimeMillis()-born)/1800f);
      p.setShader(new LinearGradient(0,0,0,h,0xff071c17,0xff174b3b,Shader.TileMode.CLAMP));c.drawRect(0,0,w,h,p);p.setShader(null);
      float cx=w*.5f,top=h*.08f;
      p.setShader(new RadialGradient(cx,top,Math.max(w,h)*.75f,new int[]{0x88fff2c6,0x33fff0b0,0x00fff0b0},null,Shader.TileMode.CLAMP));c.drawRect(0,0,w,h,p);p.setShader(null);
      p.setColor(0x22fff5d8);p.setStrokeWidth(dp(2));
      for(int i=-4;i<=4;i++){Path ray=new Path();ray.moveTo(cx,top);ray.lineTo(cx+i*w*.18f,h*.82f);c.drawPath(ray,p);}
      float alpha=t; p.setAlpha((int)(255*alpha));
      float bodyTop=h*.28f,base=h*.77f,headY=bodyTop+h*.06f;
      p.setShader(new RadialGradient(cx,headY,dp(78),new int[]{0xffffffff,0x99fff0c4,0x00fff0c4},null,Shader.TileMode.CLAMP));c.drawCircle(cx,headY,dp(78),p);p.setShader(null);
      p.setColor(0xffdcebdc);
      Path robe=new Path();robe.moveTo(cx-dp(17),headY+dp(28));robe.quadTo(cx-dp(55),bodyTop+dp(110),cx-dp(92),base);robe.quadTo(cx,base+dp(22),cx+dp(92),base);robe.quadTo(cx+dp(55),bodyTop+dp(110),cx+dp(17),headY+dp(28));robe.close();
      p.setShader(new LinearGradient(cx-dp(90),0,cx+dp(90),0,0xfff7f0d9,0xff5b8b72,Shader.TileMode.CLAMP));c.drawPath(robe,p);p.setShader(null);
      p.setColor(0x77ffffff);p.setStrokeWidth(dp(3));c.drawLine(cx-dp(18),headY+dp(40),cx-dp(55),base-dp(10),p);c.drawLine(cx+dp(18),headY+dp(40),cx+dp(55),base-dp(10),p);
      p.setColor(0xffc9ad70);p.setStrokeWidth(dp(4));c.drawLine(cx+dp(65),headY+dp(38),cx+dp(83),base+dp(5),p);c.drawCircle(cx+dp(65),headY+dp(38),dp(5),p);
      p.setAlpha(255);p.setColor(0x44e6d5ae);for(int i=0;i<28;i++){float x=(i*73%Math.max(1,(int)w)),y=(i*127%Math.max(1,(int)h));c.drawCircle(x,y,dp(1+i%3),p);}
      postInvalidateDelayed(40);
    }
  }
}
}
    protected void onDraw(Canvas c){float w=getWidth(),h=getHeight();p.setShader(new LinearGradient(0,0,w,h,which==0?0xffd8c39a:0xffb8cfc0,which==0?0xfff2e8d2:0xffe4eadf,Shader.TileMode.CLAMP));c.drawRoundRect(0,0,w,h,dp(18),dp(18),p);p.setShader(null);
      p.setColor(which==0?0xff31594b:0xff315b4c);c.drawCircle(w*.5f,h*.43f,w*.23f,p);p.setColor(0xfff4e5bd);c.drawCircle(w*.58f,h*.35f,w*.17f,p);p.setColor(which==0?0xffd8c39a:0xffb8cfc0);c.drawCircle(w*.66f,h*.29f,w*.15f,p);
      p.setColor(0xff31594b);Path dome=new Path();dome.moveTo(w*.18f,h*.75f);dome.quadTo(w*.5f,h*.28f,w*.82f,h*.75f);dome.close();c.drawPath(dome,p);c.drawRect(w*.18f,h*.73f,w*.82f,h*.87f,p);
      p.setColor(0xffe6d5ae);for(int i=0;i<3;i++)c.drawCircle(w*(.32f+i*.18f),h*.79f,dp(2),p);
    }
  }
}