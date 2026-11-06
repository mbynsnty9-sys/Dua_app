package ir.mahdi.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.Locale;

public class MainActivity extends Activity {
    private final int forest = Color.rgb(20, 55, 47);
    private final int forest2 = Color.rgb(34, 79, 67);
    private final int gold = Color.rgb(183, 145, 79);
    private final int paper = Color.rgb(248, 246, 239);
    private final int ink = Color.rgb(48, 48, 44);

    private final String[] titles = {"دعای فرج", "صلوات", "ذکر استغفار"};
    private final String[] subtitles = {"برای تعجیل در فرج امام زمان (عج)", "درود بر پیامبر و خاندان ایشان", "طلب آمرزش و بازگشت به سوی خدا"};
    private final String[] arabic = {
        "اللَّهُمَّ كُنْ لِوَلِيِّكَ الْحُجَّةِ بْنِ الْحَسَنِ، صَلَوَاتُكَ عَلَيْهِ وَعَلَى آبَائِهِ، فِي هذِهِ السَّاعَةِ وَفِي كُلِّ سَاعَةٍ، وَلِيّاً وَحَافِظاً، وَقَائِداً وَنَاصِراً، وَدَلِيلاً وَعَيْناً، حَتَّى تُسْكِنَهُ أَرْضَكَ طَوْعاً، وَتُمَتِّعَهُ فِيهَا طَوِيلاً.",
        "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَآلِ مُحَمَّدٍ",
        "أَسْتَغْفِرُ اللَّهَ رَبِّي وَأَتُوبُ إِلَيْهِ"
    };
    private final String[] persian = {
        "خدایا، برای ولیّ خود حجت بن الحسن، در این ساعت و در همه ساعت‌ها سرپرست و نگهبان و پیشوا و یاور و راهنما و دیده‌بان باش، تا او را در زمین خود با رغبت جای دهی و زمانی طولانی بهره‌مند سازی.",
        "خدایا بر محمد و خاندان محمد درود فرست.",
        "از خداوند، پروردگارم، آمرزش می‌خواهم و به سوی او بازمی‌گردم."
    };

    private LinearLayout page;
    private ScrollView readingScroll;
    private final Handler handler = new Handler();
    private boolean autoScroll = false;
    private int currentIndex = 0;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(forest);
        getWindow().setNavigationBarColor(forest);
        showHome();
    }

    private GradientDrawable bg(int color, int radius, int strokeColor) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        if (strokeColor != 0) d.setStroke(1, strokeColor);
        return d;
    }

    private TextView label(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setTextDirection(View.TEXT_DIRECTION_RTL);
        t.setPadding(16, 12, 16, 12);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    private LinearLayout.LayoutParams margins(int top, int bottom) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(16, top, 16, bottom);
        return p;
    }

    private TextView action(String text) {
        TextView t = label(text, 15, Color.WHITE, true);
        t.setGravity(Gravity.CENTER);
        t.setBackground(bg(forest2, 24, 0));
        return t;
    }

    private void showHome() {
        autoScroll = false;
        page = column();
        page.setBackgroundColor(paper);

        LinearLayout hero = column();
        hero.setPadding(22, 24, 22, 26);
        hero.setBackground(bg(forest, 0, 0));
        TextView overline = label("کتابخانهٔ همراه", 13, 0xffd8c49c, true);
        TextView title = label("گنجینهٔ ادعیه و مناجات", 25, Color.WHITE, true);
        TextView caption = label("متن دعاها، ترجمه و صوت در یک‌جا", 14, 0xffe5e8e1, false);
        hero.addView(overline);
        hero.addView(title);
        hero.addView(caption);
        page.addView(hero);

        TextView section = label("انتخاب دعا", 19, forest, true);
        section.setPadding(20, 22, 20, 8);
        page.addView(section);

        EditText search = new EditText(this);
        search.setSingleLine(true);
        search.setTextSize(15);
        search.setTextDirection(View.TEXT_DIRECTION_RTL);
        search.setHint("جست‌وجو در عنوان ادعیه...");
        search.setPadding(18, 8, 18, 8);
        search.setBackground(bg(Color.WHITE, 22, 0xffe7e0d2));
        page.addView(search, margins(6, 10));

        LinearLayout list = column();
        page.addView(list);
        for (int i = 0; i < titles.length; i++) addDuaCard(list, i);

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int st, int before, int count) {
                String q = s.toString().trim().toLowerCase(new Locale("fa"));
                list.removeAllViews();
                for (int i = 0; i < titles.length; i++) {
                    if (titles[i].toLowerCase(new Locale("fa")).contains(q) ||
                        subtitles[i].toLowerCase(new Locale("fa")).contains(q)) addDuaCard(list, i);
                }
            }
            @Override public void afterTextChanged(Editable e) {}
        });

        TextView footer = label("نسخهٔ آزمایشی • محتوای بیشتر در مراحل بعد اضافه می‌شود", 12, 0xff88877f, false);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(10, 20, 10, 24);
        page.addView(footer);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(page);
        setContentView(scroll);
    }

    private void addDuaCard(LinearLayout list, int index) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(12, 12, 12, 12);
        card.setBackground(bg(Color.WHITE, 22, 0xffe9e3d7));

        TextView number = label(String.format(Locale.US, "%02d", index + 1), 15, gold, true);
        number.setGravity(Gravity.CENTER);
        number.setBackground(bg(0xfff4efe4, 18, 0));
        LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(48, 48);
        card.addView(number, np);

        LinearLayout words = column();
        TextView name = label(titles[index], 17, forest, true);
        TextView desc = label(subtitles[index], 12, 0xff77766f, false);
        words.addView(name);
        words.addView(desc);
        card.addView(words, new LinearLayout.LayoutParams(0, -2, 1));

        TextView arrow = label("‹", 28, gold, true);
        arrow.setGravity(Gravity.CENTER);
        card.addView(arrow, new LinearLayout.LayoutParams(32, -2));

        card.setOnClickListener(v -> showDua(index));
        list.addView(card, margins(5, 7));
    }

    private void showDua(int index) {
        currentIndex = index;
        autoScroll = false;
        page = column();
        page.setPadding(0, 0, 0, 18);
        page.setBackgroundColor(paper);

        TextView back = action("بازگشت به فهرست");
        back.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        back.setPadding(20, 16, 20, 16);
        page.addView(back);
        back.setOnClickListener(v -> showHome());

        TextView heading = label(titles[index], 23, forest, true);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(18, 18, 18, 4);
        page.addView(heading);

        TextView sub = label(subtitles[index], 13, 0xff77766f, false);
        sub.setGravity(Gravity.CENTER);
        page.addView(sub);

        readingScroll = new ScrollView(this);
        LinearLayout content = column();

        TextView arabicText = label(arabic[index], 27, forest, true);
        arabicText.setGravity(Gravity.CENTER);
        arabicText.setTextDirection(View.TEXT_DIRECTION_RTL);
        arabicText.setLineSpacing(14, 1.15f);
        arabicText.setPadding(22, 28, 22, 28);
        arabicText.setBackground(bg(Color.WHITE, 24, 0xffe9e3d7));
        content.addView(arabicText, margins(18, 12));

        TextView translationTitle = label("ترجمهٔ فارسی", 15, gold, true);
        content.addView(translationTitle, margins(8, 2));
        TextView translation = label(persian[index], 17, ink, false);
        translation.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        translation.setLineSpacing(8, 1.1f);
        translation.setPadding(22, 22, 22, 22);
        translation.setBackground(bg(0xfff0ebdf, 22, 0));
        content.addView(translation, margins(4, 18));

        readingScroll.addView(content);
        page.addView(readingScroll, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout controls = new LinearLayout(this);
        controls.setPadding(16, 8, 16, 8);
        controls.setGravity(Gravity.CENTER);
        TextView scrollButton = action("شروع حرکت خودکار");
        controls.addView(scrollButton, new LinearLayout.LayoutParams(-1, 52));
        scrollButton.setOnClickListener(v -> {
            autoScroll = !autoScroll;
            scrollButton.setText(autoScroll ? "توقف حرکت خودکار" : "شروع حرکت خودکار");
            if (autoScroll) runAutoScroll();
        });
        page.addView(controls);

        TextView audioNote = label("برای این دعا هنوز فایل صوتی ثبت نشده است.", 12, 0xff88877f, false);
        audioNote.setGravity(Gravity.CENTER);
        page.addView(audioNote);

        ScrollView outer = new ScrollView(this);
        outer.addView(page);
        setContentView(outer);
    }

    private void runAutoScroll() {
        handler.postDelayed(new Runnable() {
            @Override public void run() {
                if (!autoScroll || readingScroll == null) return;
                int max = readingScroll.getChildAt(0).getHeight() - readingScroll.getHeight();
                if (readingScroll.getScrollY() >= max) {
                    autoScroll = false;
                    return;
                }
                readingScroll.smoothScrollBy(0, 2);
                runAutoScroll();
            }
        }, 70);
    }

    @Override public void onBackPressed() {
        if (currentIndex >= 0) {
            currentIndex = -1;
            showHome();
        } else {
            super.onBackPressed();
        }
    }
}