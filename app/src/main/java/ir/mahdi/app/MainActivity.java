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
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.media.MediaPlayer;
import org.json.*;
import java.io.*;
import java.util.*;

public class MainActivity extends Activity {
  final int forest=0xff173d34, forest2=0xff245b4b, gold=0xffb99250, paper=0xfff7f4ec, ink=0xff33332f;
  final int PICK_AUDIO=10, PICK_FONT=11, PICK_IMAGE=12;
  ArrayList<Dua> duas=new ArrayList<>();
  LinearLayout page;
  MediaPlayer player;
  Typeface arabicTypeface=Typeface.create("serif",Typeface.NORMAL);
  String defaultAudio="";
  int selected=-1;
  boolean auto=false;
  Handler handler=new Handler();

  static class Dua {
    String id,title,category,arabic,persian,audio,image;
    Dua(String t,String c,String a,String p){id=UUID.randomUUID().toString();title=t;category=c;arabic=a;persian=p;audio="";image="";}
    JSONObject json() throws Exception {JSONObject o=new JSONObject();o.put("id",id);o.put("title",title);o.put("category",category);o.put("arabic",arabic);o.put("persian",persian);o.put("audio",audio);o.put("image",image);return o;}
    static Dua from(JSONObject o){Dua d=new Dua(o.optString("title"),o.optString("category"),o.optString("arabic"),o.optString("persian"));d.id=o.optString("id",d.id);d.audio=o.optString("audio");d.image=o.optString("image");return d;}
  }

  @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(forest);getWindow().setNavigationBarColor(forest);loadData();showHome();}
  int dp(float v){return (int)(v*getResources().getDisplayMetrics().density+.5f);}
  GradientDrawable shape(int c,int r,int stroke){GradientDrawable d=new GradientDrawable();d.setColor(c);d.setCornerRadius(dp(r));if(stroke!=0)d.setStroke(dp(1),stroke);return d;}
  TextView text(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setGravity(Gravity.CENTER_VERTICAL);t.setTextDirection(View.TEXT_DIRECTION_RTL);t.setPadding(dp(14),dp(10),dp(14),dp(10));if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
  TextView button(String s){TextView t=text(s,14,Color.WHITE,true);t.setGravity(Gravity.CENTER);t.setBackground(shape(forest2,18,0));return t;}
  LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(1);return l;}
  LinearLayout row(){LinearLayout l=new LinearLayout(this);l.setOrientation(0);l.setGravity(Gravity.CENTER_VERTICAL);return l;}
  LinearLayout.LayoutParams lp(int w,int h){return new LinearLayout.LayoutParams(w<0?w:dp(w),h<0?h:dp(h));}
  LinearLayout.LayoutParams margin(int top,int bottom){LinearLayout.LayoutParams p=lp(-1,-2);p.setMargins(dp(15),dp(top),dp(15),dp(bottom));return p;}

  void loadData(){
    android.content.SharedPreferences sp=getSharedPreferences("dua_store",0);
    defaultAudio=sp.getString("default_audio","");
    String font=sp.getString("font","");
    if(!font.isEmpty())try{arabicTypeface=Typeface.createFromFile(font);}catch(Exception ignored){}
    String saved=sp.getString("duas","");
    if(!saved.isEmpty())try{JSONArray a=new JSONArray(saved);for(int i=0;i<a.length();i++)duas.add(Dua.from(a.getJSONObject(i)));}catch(Exception ignored){}
    if(duas.isEmpty())seed();
  }
  void seed(){
    addSeed("دعای فرج","امام زمان (عج)","اللَّهُمَّ كُنْ لِوَلِيِّكَ الْحُجَّةِ بْنِ الْحَسَنِ، صَلَوَاتُكَ عَلَيْهِ وَعَلَى آبَائِهِ، فِي هذِهِ السَّاعَةِ وَفِي كُلِّ سَاعَةٍ، وَلِيّاً وَحَافِظاً، وَقَائِداً وَنَاصِراً، وَدَلِيلاً وَعَيْناً، حَتَّى تُسْكِنَهُ أَرْضَكَ طَوْعاً، وَتُمَتِّعَهُ فِيهَا طَوِيلاً.","خدایا، در این ساعت و در همه ساعت‌ها، سرپرست و نگهبان و پیشوا و یاور و راهنمای ولیّ خود، حجت بن الحسن باش؛ تا او را با رغبت در زمینت جای دهی و زمانی طولانی بهره‌مند سازی.");
    addSeed("صلوات","ذکر روزانه","اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَآلِ مُحَمَّدٍ","خدایا، بر محمد و خاندان محمد درود فرست.");
    addSeed("استغفار","طلب آمرزش","أَسْتَغْفِرُ اللَّهَ رَبِّي وَأَتُوبُ إِلَيْهِ","از خداوند، پروردگارم، آمرزش می‌خواهم و به سوی او بازمی‌گردم.");
    addSeed("آیة الکرسی","قرآن کریم","اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ","خداست که معبودی جز او نیست؛ زنده و برپادارنده است. نه خواب سبک او را فرا می‌گیرد و نه خواب سنگین. آنچه در آسمان‌ها و زمین است از آنِ اوست. چه کسی جز به اذن او نزدش شفاعت می‌کند؟ آنچه پیش روی مردم و پشت سرشان است می‌داند و آنان به چیزی از دانش او جز آنچه بخواهد احاطه ندارند.");
    addSeed("دعای توسل","توسل","اللَّهُمَّ إِنِّي أَسْأَلُكَ وَأَتَوَجَّهُ إِلَيْكَ بِنَبِيِّكَ نَبِيِّ الرَّحْمَةِ مُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَآلِهِ","خدایا، از تو درخواست می‌کنم و به‌وسیله پیامبرت، پیامبر رحمت، محمد (ص)، به تو روی می‌آورم.");
    addSeed("دعای عهد","امام زمان (عج)","اللَّهُمَّ رَبَّ النُّورِ الْعَظِيمِ، وَرَبَّ الْكُرْسِيِّ الرَّفِيعِ، وَرَبَّ الْبَحْرِ الْمَسْجُورِ","خدایا، ای پروردگار نور بزرگ، کرسی بلندمرتبه و دریای افروخته.");
    addSeed("دعای کمیل","مناجات","اللَّهُمَّ إِنِّي أَسْأَلُكَ بِرَحْمَتِكَ الَّتِي وَسِعَتْ كُلَّ شَيْءٍ","خدایا، از تو به حق رحمتت که همه چیز را فرا گرفته درخواست می‌کنم.");
    addSeed("دعای صباح","امام علی (ع)","يَا مَنْ دَلَعَ لِسَانَ الصَّبَاحِ بِنُطْقِ تَبَلُّجِهِ","ای آن‌که زبان صبح را با روشنایی و درخشش آن گویا ساختی.");
    addSeed("دعای سلامتی امام زمان","امام زمان (عج)","اللَّهُمَّ كُنْ لِوَلِيِّكَ الْحَافِظاً وَقَائِداً وَنَاصِراً","خدایا، برای ولیّ خود نگهبان و راهبر و یاور باش.");
    addSeed("تسبیحات حضرت زهرا","ذکر روزانه","سُبْحَانَ اللَّهِ، وَالْحَمْدُ لِلَّهِ، وَلَا إِلَهَ إِلَّا اللَّهُ، وَاللَّهُ أَكْبَرُ","پاک و منزه است خدا؛ ستایش مخصوص خداست؛ معبودی جز خدا نیست؛ و خدا بزرگ‌تر است.");
    saveData();
  }
  void addSeed(String t,String c,String a,String p){duas.add(new Dua(t,c,a,p));}
  void saveData(){try{JSONArray a=new JSONArray();for(Dua d:duas)a.put(d.json());getSharedPreferences("dua_store",0).edit().putString("duas",a.toString()).putString("default_audio",defaultAudio).apply();}catch(Exception ignored){}}

  void showHome(){
    auto=false;stopAudio();page=column();page.setBackgroundColor(paper);
    LinearLayout hero=column();hero.setPadding(dp(20),dp(22),dp(20),dp(24));hero.setBackground(shape(forest,0,0));
    hero.addView(text("کتابخانهٔ شخصی شما",13,0xffd8c49c,true));hero.addView(text("گنجینهٔ ادعیه و مناجات",25,Color.WHITE,true));hero.addView(text("متن، ترجمه، صوت و ابزار مطالعه",14,0xffe6e9e2,false));page.addView(hero);
    LinearLayout tools=row();tools.setPadding(dp(12),dp(14),dp(12),dp(6));
    TextView add=button("+ افزودن دعا"), settings=button("تنظیمات و فونت");
    tools.addView(add,new LinearLayout.LayoutParams(0,dp(48),1));View gap=new View(this);tools.addView(gap,lp(8,1));tools.addView(settings,new LinearLayout.LayoutParams(0,dp(48),1));page.addView(tools);
    add.setOnClickListener(v->editDua(-1));settings.setOnClickListener(v->settingsDialog());
    EditText search=new EditText(this);search.setSingleLine(true);search.setTextSize(15);search.setTextDirection(View.TEXT_DIRECTION_RTL);search.setHint("جست‌وجو در دعاها و دسته‌بندی‌ها");search.setPadding(dp(16),dp(5),dp(16),dp(5));search.setBackground(shape(Color.WHITE,20,0xffe5ddce));page.addView(search,margin(8,8));
    LinearLayout list=column();page.addView(list);
    renderList(list,"");
    search.addTextChangedListener(new android.text.TextWatcher(){public void beforeTextChanged(CharSequence s,int st,int c,int a){}public void onTextChanged(CharSequence s,int st,int before,int count){renderList(list,s.toString().trim());}public void afterTextChanged(android.text.Editable e){}});
    TextView foot=text("برای افزودن محتوای شخصی، از دکمهٔ افزودن دعا استفاده کنید.",12,0xff88877f,false);foot.setGravity(Gravity.CENTER);page.addView(foot);
    ScrollView sc=new ScrollView(this);sc.setFillViewport(true);sc.addView(page);setContentView(sc);
  }
  void renderList(LinearLayout list,String q){
    list.removeAllViews();int count=0;
    for(int i=0;i<duas.size();i++){final int index=i;Dua d=duas.get(i);if(!d.title.contains(q)&&!d.category.contains(q))continue;count++;
      LinearLayout card=row();card.setPadding(dp(12),dp(10),dp(12),dp(10));card.setBackground(shape(Color.WHITE,20,0xffe8e1d5));
      TextView badge=text(String.format(Locale.US,"%02d",i+1),14,gold,true);badge.setGravity(Gravity.CENTER);badge.setBackground(shape(0xfff4efe4,16,0));card.addView(badge,lp(46,46));
      LinearLayout info=column();info.addView(text(d.title,16,forest,true));info.addView(text(d.category+(d.audio.isEmpty()&&defaultAudio.isEmpty()?"  •  بدون صوت":"  •  صوت موجود"),12,0xff77766f,false));card.addView(info,new LinearLayout.LayoutParams(0,-2,1));
      TextView arrow=text("‹",26,gold,true);card.addView(arrow,lp(28,-2));card.setOnClickListener(v->showDua(index));card.setOnLongClickListener(v->{duaMenu(index);return true;});list.addView(card,margin(5,6));
    }
    if(count==0){TextView empty=text("موردی پیدا نشد.",14,0xff777777,false);empty.setGravity(Gravity.CENTER);list.addView(empty);}
  }

  void showDua(int index){
    selected=index;auto=false;stopAudio();Dua d=duas.get(index);page=column();page.setBackgroundColor(paper);
    TextView back=button("بازگشت به فهرست");page.addView(back,margin(12,4));back.setOnClickListener(v->showHome());
    TextView title=text(d.title,23,forest,true);title.setGravity(Gravity.CENTER);page.addView(title);
    TextView category=text(d.category,12,gold,true);category.setGravity(Gravity.CENTER);page.addView(category);
    if(!d.image.isEmpty()){try{ImageView iv=new ImageView(this);iv.setAdjustViewBounds(true);iv.setMaxHeight(dp(180));iv.setImageURI(Uri.fromFile(new File(d.image)));page.addView(iv,margin(8,4));}catch(Exception ignored){}}
    ScrollView read=new ScrollView(this);LinearLayout content=column();
    String[] chunks=d.arabic.split("\\n\\s*\\n");
    String[] translations=d.persian.split("\\n\\s*\\n");
    for(int i=0;i<chunks.length;i++){
      TextView a=text(chunks[i],27,forest,true);a.setTypeface(arabicTypeface,Typeface.BOLD);a.setGravity(Gravity.CENTER);a.setLineSpacing(dp(9),1.15f);a.setPadding(dp(20),dp(22),dp(20),dp(22));a.setBackground(shape(Color.WHITE,22,0xffe8e1d5));content.addView(a,margin(12,5));
      if(i<translations.length){TextView tr=text(translations[i],17,ink,false);tr.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);tr.setLineSpacing(dp(6),1.1f);tr.setBackground(shape(0xfff0ebdf,20,0));content.addView(tr,margin(4,10));}
    }
    read.addView(content);page.addView(read,new LinearLayout.LayoutParams(-1,0,1));
    LinearLayout controls=row();controls.setPadding(dp(12),dp(6),dp(12),dp(6));
    TextView play=button("▶ پخش صوت"), scroll=button("حرکت خودکار"), more=button("مدیریت");
    controls.addView(play,new LinearLayout.LayoutParams(0,dp(48),1));controls.addView(space(6),lp(6,1));controls.addView(scroll,new LinearLayout.LayoutParams(0,dp(48),1));controls.addView(space(6),lp(6,1));controls.addView(more,new LinearLayout.LayoutParams(0,dp(48),1));page.addView(controls);
    play.setOnClickListener(v->playAudio(index,play));scroll.setOnClickListener(v->{auto=!auto;scroll.setText(auto?"توقف":"حرکت خودکار");if(auto)autoScroll(read,scroll);});more.setOnClickListener(v->duaMenu(index));
    setContentView(page);
  }
  View space(int w){View v=new View(this);return v;}
  void autoScroll(ScrollView s,TextView b){handler.postDelayed(new Runnable(){public void run(){if(!auto)return;int max=s.getChildAt(0).getHeight()-s.getHeight();if(s.getScrollY()>=max){auto=false;b.setText("حرکت خودکار");return;}s.smoothScrollBy(0,dp(2));autoScroll(s,b);}},80);}

  void duaMenu(int i){
    String[] opts={"ویرایش متن و عنوان","انتخاب صوت اختصاصی (حداکثر ۳۰ مگابایت)","انتخاب تصویر دعا","حذف دعا"};
    new AlertDialog.Builder(this).setTitle(duas.get(i).title).setItems(opts,(dlg,which)->{
      if(which==0)editDua(i);
      if(which==1){selected=i;pick(PICK_AUDIO,"audio/*");}
      if(which==2){selected=i;pick(PICK_IMAGE,"image/*");}
      if(which==3)new AlertDialog.Builder(this).setMessage("این دعا از فهرست شخصی حذف شود؟").setNegativeButton("انصراف",null).setPositiveButton("حذف",(x,y)->{duas.remove(i);saveData();showHome();}).show();
    }).show();
  }

  void editDua(int index){
    boolean fresh=index<0;Dua d=fresh?new Dua("","","",""):duas.get(index);
    LinearLayout form=column();form.setPadding(dp(8),dp(4),dp(8),dp(4));
    EditText title=field("عنوان دعا",d.title,false),cat=field("دسته‌بندی / منبع",d.category,false),arab=field("متن عربی (برای جدا کردن بندها یک خط خالی بگذارید)",d.arabic,true),pers=field("ترجمهٔ فارسی (بندها را به همان ترتیب جدا کنید)",d.persian,true);
    form.addView(title);form.addView(cat);form.addView(arab);form.addView(pers);
    ScrollView sc=new ScrollView(this);sc.addView(form);
    new AlertDialog.Builder(this).setTitle(fresh?"افزودن دعای جدید":"ویرایش دعا").setView(sc).setNegativeButton("انصراف",null).setPositiveButton("ذخیره",(x,y)->{
      if(title.getText().toString().trim().isEmpty()||arab.getText().toString().trim().isEmpty()){toast("عنوان و متن عربی الزامی است.");return;}
      d.title=title.getText().toString().trim();d.category=cat.getText().toString().trim();d.arabic=arab.getText().toString().trim();d.persian=pers.getText().toString().trim();if(fresh)duas.add(d);saveData();showHome();
    }).show();
  }
  EditText field(String hint,String value,boolean multi){EditText e=new EditText(this);e.setText(value);e.setHint(hint);e.setTextSize(15);e.setTextDirection(View.TEXT_DIRECTION_RTL);e.setGravity(Gravity.RIGHT|Gravity.TOP);e.setPadding(dp(12),dp(10),dp(12),dp(10));e.setBackground(shape(Color.WHITE,14,0xffded8cd));LinearLayout.LayoutParams p=margin(5,5);if(multi){e.setMinLines(3);e.setInputType( android.text.InputType.TYPE_CLASS_TEXT|android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);}else e.setSingleLine(true);e.setLayoutParams(p);return e;}

  void settingsDialog(){
    String[] options={"انتخاب فونت عربی TTF/OTF","انتخاب صوت پیش‌فرض","حذف صوت پیش‌فرض","درباره و راهنمای برنامه"};
    new AlertDialog.Builder(this).setTitle("تنظیمات کتابخانه").setItems(options,(d,w)->{
      if(w==0)pick(PICK_FONT,"font/*");
      if(w==1)pickDefaultAudio();
      if(w==2){defaultAudio="";saveData();toast("صوت پیش‌فرض حذف شد.");showHome();}
      if(w==3)new AlertDialog.Builder(this).setTitle("راهنما").setMessage("متن‌ها و فایل‌های واردشده روی همین دستگاه نگهداری می‌شوند. برای هر دعا می‌توانید صوت و تصویر جداگانه انتخاب کنید. حجم هر فایل صوتی حداکثر ۳۰ مگابایت است.").setPositiveButton("متوجه شدم",null).show();
    }).show();
  }
  void pickDefaultAudio(){selected=-1;pick(PICK_AUDIO,"audio/*");}
  void pick(int request,String type){Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.addCategory(Intent.CATEGORY_OPENABLE);i.setType(type);startActivityForResult(i,request);}
  @Override protected void onActivityResult(int req,int result,Intent data){
    super.onActivityResult(req,result,data);if(result!=RESULT_OK||data==null||data.getData()==null)return;Uri uri=data.getData();
    try{
      if(req==PICK_AUDIO){long size=getSize(uri);if(size>30L*1024*1024){toast("حجم فایل صوتی باید حداکثر ۳۰ مگابایت باشد.");return;}String path=copyToPrivate(uri,"audio_"+System.currentTimeMillis()+".bin");if(selected<0){defaultAudio=path;saveData();toast("صوت پیش‌فرض ذخیره شد.");showHome();}else{duas.get(selected).audio=path;saveData();toast("صوت دعا ذخیره شد.");showDua(selected);}}
      else if(req==PICK_FONT){String path=copyToPrivate(uri,"custom_font.ttf");Typeface tf=Typeface.createFromFile(path);arabicTypeface=tf;getSharedPreferences("dua_store",0).edit().putString("font",path).apply();toast("فونت سفارشی ثبت شد.");if(selected>=0)showDua(selected);}
      else if(req==PICK_IMAGE&&selected>=0){String path=copyToPrivate(uri,"dua_image_"+System.currentTimeMillis()+".img");duas.get(selected).image=path;saveData();showDua(selected);toast("تصویر دعا ثبت شد.");}
    }catch(Exception e){toast("خطا در خواندن فایل: "+e.getMessage());}
  }
  long getSize(Uri u){try(Cursor c=getContentResolver().query(u,null,null,null,null)){if(c!=null&&c.moveToFirst()){int col=c.getColumnIndex(OpenableColumns.SIZE);if(col>=0)return c.getLong(col);}}return 0;}
  String copyToPrivate(Uri uri,String name)throws Exception{File f=new File(getFilesDir(),name);try(InputStream in=getContentResolver().openInputStream(uri);OutputStream out=new FileOutputStream(f)){byte[] b=new byte[8192];int n;while((n=in.read(b))>0)out.write(b,0,n);}return f.getAbsolutePath();}
  void playAudio(int i,TextView button){
    String path=!duas.get(i).audio.isEmpty()?duas.get(i).audio:defaultAudio;
    if(path.isEmpty()){toast("برای این دعا صوت اختصاصی یا صوت پیش‌فرض انتخاب نشده است.");return;}
    stopAudio();try{player=new MediaPlayer();player.setDataSource(path);player.setOnPreparedListener(mp->{mp.start();button.setText("■ توقف صوت");});player.setOnCompletionListener(mp->{button.setText("▶ پخش صوت");stopAudio();});player.prepareAsync();button.setOnClickListener(v->{stopAudio();button.setText("▶ پخش صوت");});}catch(Exception e){toast("پخش این فایل ممکن نیست.");stopAudio();}
  }
  void stopAudio(){if(player!=null){try{if(player.isPlaying())player.stop();player.release();}catch(Exception ignored){}player=null;}}
  void toast(String s){Toast.makeText(this,s,Toast.LENGTH_LONG).show();}
  @Override public void onBackPressed(){auto=false;stopAudio();showHome();}
  @Override protected void onDestroy(){auto=false;stopAudio();super.onDestroy();}
}