package com.example.data.local

import com.example.R
import com.example.data.model.NewsArticleEntity
import com.example.data.model.NewsCategory
import com.example.data.model.NewsSourceEntity

object InitialData {

    fun getDefaultSources(): List<NewsSourceEntity> = listOf(
        NewsSourceEntity(
            id = 1,
            name = "الجزيرة نت",
            websiteUrl = "https://www.aljazeera.net",
            categoryId = "politics",
            colorHex = 0xFFD97706,
            isEnabled = true,
            isCustom = false
        ),
        NewsSourceEntity(
            id = 2,
            name = "العربية",
            websiteUrl = "https://www.alarabiya.net",
            categoryId = "all",
            colorHex = 0xFF7C3AED,
            isEnabled = true,
            isCustom = false
        ),
        NewsSourceEntity(
            id = 3,
            name = "BBC عربي",
            websiteUrl = "https://www.bbc.com/arabic",
            categoryId = "world",
            colorHex = 0xFFDC2626,
            isEnabled = true,
            isCustom = false
        ),
        NewsSourceEntity(
            id = 4,
            name = "سكاي نيوز عربية",
            websiteUrl = "https://www.skynewsarabia.com",
            categoryId = "all",
            colorHex = 0xFF1D4ED8,
            isEnabled = true,
            isCustom = false
        ),
        NewsSourceEntity(
            id = 5,
            name = "كورة سبورت",
            websiteUrl = "https://www.kooora.com",
            categoryId = "sports",
            colorHex = 0xFF059669,
            isEnabled = true,
            isCustom = false
        ),
        NewsSourceEntity(
            id = 6,
            name = "البوابة التقنية",
            websiteUrl = "https://aitnews.com",
            categoryId = "tech",
            colorHex = 0xFF0D9488,
            isEnabled = true,
            isCustom = false
        ),
        NewsSourceEntity(
            id = 7,
            name = "الشرق الأوسط",
            websiteUrl = "https://aawsat.com",
            categoryId = "economy",
            colorHex = 0xFF475569,
            isEnabled = true,
            isCustom = false
        ),
        NewsSourceEntity(
            id = 8,
            name = "صحتك دوت كوم",
            websiteUrl = "https://www.sehatok.com",
            categoryId = "health",
            colorHex = 0xFF10B981,
            isEnabled = true,
            isCustom = false
        )
    )

    fun getDefaultArticles(): List<NewsArticleEntity> {
        val now = System.currentTimeMillis()
        val minute = 60 * 1000L
        val hour = 60 * minute

        return listOf(
            // --- POLITICS ---
            NewsArticleEntity(
                id = 1,
                title = "قمة دولية كبرى لبحث استقرار أسواق الطاقة والأمن العالمي",
                summary = "قادة وممثلون عن أكثر من 40 دولة يلتئمون لمناقشة سبل تعزيز التعاون الاقتصادي وتحقيق التنمية المستدامة وحفظ الاستقرار الإقليمي.",
                content = """افتتحت اليوم أعمال القمة العالمية رفيعة المستوى بمشاركة وفود رسمية وقادة دول من مختلف القارات، لبحث التحديات الراهنة في ملفات الطاقة وسلاسل الإمداد ومواجهة التغير المناخي.

وأكد المشاركون في الجلسة الافتتاحية على ضرورة تضافر الجهود المشتركة لدعم التحول نحو الطاقة النظيفة، وضمان استقرار الأسواق العالمية وحماية الأمن الغذائي في ظل المتغيرات الجيوسياسية المتسارعة.

وتتضمن القمة جلسات حوارية متخصصة تجمع وزراء المالية والطاقة مع ممثلي كبرى المنظمات الاقتصادية الدولية لمناقشة استراتيجيات الاستثمار في البنية التحتية والذكاء الاصطناعي المسؤول.""",
                categoryId = NewsCategory.POLITICS.id,
                sourceName = "الجزيرة نت",
                sourceUrl = "https://www.aljazeera.net",
                imageUrl = "https://images.unsplash.com/photo-1541872703-74c5e44368f9?w=800&q=80",
                localDrawableResId = R.drawable.news_world_banner_1787575074316,
                publishedTimestamp = now - 15 * minute,
                isBookmarked = false,
                isBreaking = true,
                readCount = 342,
                readingTimeMinutes = 4,
                author = "قسم الشؤون الدولية"
            ),
            NewsArticleEntity(
                id = 2,
                title = "مشروع اتفاقية دبلوماسية جديدة لتعزيز الملاحة والتجارة البحرية",
                summary = "توافق دولي واسع على بنود جديدة تحمي الممرات المائية الحيوية وتضمن انسيابية حركة التجارة الدولية دون عوائق.",
                content = """توصلت أطراف المفاوضات البحرية الدولية إلى مسودة اتفاقية شاملة تركز على تدعيم سلامة الممرات البحرية التجارية ووضع آليات استجابة سريعة للطوارئ.

وتشمل الاتفاقية الجديدة بنوداً تنص على حماية البيئة البحرية، والحد من الانبعاثات الكربونية لسفن الشحن العملاقة، واعتماد تقنيات المراقبة الرقمية بالأقمار الاصطناعية لضمان أقصى درجات الأمان الملاحي.""",
                categoryId = NewsCategory.POLITICS.id,
                sourceName = "سكاي نيوز عربية",
                sourceUrl = "https://www.skynewsarabia.com",
                imageUrl = "https://images.unsplash.com/photo-1526470608268-f674ce90ebd4?w=800&q=80",
                localDrawableResId = R.drawable.news_world_banner_1787575074316,
                publishedTimestamp = now - 2 * hour,
                isBookmarked = false,
                isBreaking = false,
                readCount = 210,
                readingTimeMinutes = 3,
                author = "مراسلنا في جنيف"
            ),

            // --- SPORTS ---
            NewsArticleEntity(
                id = 3,
                title = "ريال مدريد ومانشستر سيتي في مواجهة نارية لحسم بطاقة العبور القاري",
                summary = "ترقب جماهيري حاشد للقمة الأوروبية المرتقبة، والمدربون يضعون اللمسات التكتيكية الأخيرة في المؤتمرات الصحفية.",
                content = """تتجه أنظار عشاق الساحرة المستديرة مساء اليوم إلى الملعب الأسطوري الذي يحتضن مواجهة العمالقة بين ريال مدريد ومانشستر سيتي في إياب الدور الحاسم للبطولة القارية.

وأكد المدير الفني خلال المؤتمر الصحفي جاهزية جميع النجوم الأساسيين، مشيراً إلى أن التفاصيل الصغيرة والتركيز الذهني طوال الـ 90 دقيقة ستكون هي الفيصل في حسم بطاقة التأهل للنهائي الكبير.

وشهدت تدريبات الفريقين حماساً لافتاً مع التركيز على الكرات الثابتة والضغط العالي في مناطق الخصم لاقتناص الأهداف المبكرة.""",
                categoryId = NewsCategory.SPORTS.id,
                sourceName = "كورة سبورت",
                sourceUrl = "https://www.kooora.com",
                imageUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=800&q=80",
                localDrawableResId = R.drawable.news_sports_banner_1787575039663,
                publishedTimestamp = now - 35 * minute,
                isBookmarked = true,
                isBreaking = true,
                readCount = 890,
                readingTimeMinutes = 3,
                author = "محرر كرة القدم"
            ),
            NewsArticleEntity(
                id = 4,
                title = "إنجاز تاريخي للسباحة العربية بحصد ميداليتين ذهبيتين في بطولة العالم",
                summary = "تألق باهر للأبطال العرب في سباقي 400 متر حرة و200 متر فراشة وتسجيل أرقام قياسية جديدة تلفت أنظار العالم.",
                content = """سجلت السباحة العربية إنجازاً تاريخياً مشرفاً في بطولة العالم للألعاب المائية المقامة حالياً، بعد التتويج بذهبيتين وسط منافسة شرسة مع أبطال اللعبة الأولمبيين.

وحطم البطل العربي الرقم القياسي للسباق بفارق ثانية ونصف، ليؤكد المكانة الرياضية العالمية المرموقة والتحضير البدني المكثف الذي سبق انطلاق البطولة العالمية.""",
                categoryId = NewsCategory.SPORTS.id,
                sourceName = "العربية",
                sourceUrl = "https://www.alarabiya.net",
                imageUrl = "https://images.unsplash.com/photo-1530549387789-4c1017266635?w=800&q=80",
                localDrawableResId = R.drawable.news_sports_banner_1787575039663,
                publishedTimestamp = now - 3 * hour,
                isBookmarked = false,
                isBreaking = false,
                readCount = 412,
                readingTimeMinutes = 2,
                author = "موفد القسم الرياضي"
            ),

            // --- TECH ---
            NewsArticleEntity(
                id = 5,
                title = "إطلاق ثورة جديدة في نماذج الذكاء الاصطناعي التوليدي بقدرات تفكير متقدمة",
                summary = "إعلان رسمي عن الجيل الجديد من المعالجات العصبية وخوارزميات التفكير المنطقي الفائقة السرعة للأجهزة المحمولة.",
                content = """كشفت كبرى شركات التقنية العالمية النقاب عن أحدث نماذج الذكاء الاصطناعي المبتكرة، والتي تمتاز بقدرتها على حل المسائل المعقدة والبرمجة التلقائية وتحليل البيانات الضخمة في أجزاء من الثانية.

وتعمل النماذج الجديدة بكفاءة عالية على الهواتف الذكية دون استهلاك مفرط للطاقة، مما يتيح تجربة مساعد شخصي فائق الذكاء يعمل محلياً ويحفظ خصوصية المستخدم التامة.""",
                categoryId = NewsCategory.TECH.id,
                sourceName = "البوابة التقنية",
                sourceUrl = "https://aitnews.com",
                imageUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&q=80",
                localDrawableResId = R.drawable.news_tech_banner_1787575055120,
                publishedTimestamp = now - 50 * minute,
                isBookmarked = true,
                isBreaking = false,
                readCount = 1250,
                readingTimeMinutes = 4,
                author = "خبير التكنولوجيا"
            ),
            NewsArticleEntity(
                id = 6,
                title = "بطاريات الحالة الصلبة للسيارات الكهربائية تقترب من مرحلة الإنتاج التجاري",
                summary = "شحن كامل في 10 دقائق ومدى قيادة يتجاوز 1000 كيلومتر، نقلة نوعية ستغير مستقبل النقل المستدام عالمياً.",
                content = """أعلنت مختبرات التطوير الصناعي عن اجتياز بطاريات الحالة الصلبة الجديدة لكافة اختبارات السلامة والأداء المكثفة بنجاح تام.

وتتميز هذه التقنية بمقاومة فائقة للحرارة وكثافة طاقة مضاعفة مقارنة ببطاريات الليثيوم الحالية، مع توقعات ببدء تزويد خطوط الإنتاج للسيارات الجديدة بها مطلع العام القادم.""",
                categoryId = NewsCategory.TECH.id,
                sourceName = "الشرق الأوسط",
                sourceUrl = "https://aawsat.com",
                imageUrl = "https://images.unsplash.com/photo-1593941707882-a5bba14938c7?w=800&q=80",
                localDrawableResId = R.drawable.news_tech_banner_1787575055120,
                publishedTimestamp = now - 4 * hour,
                isBookmarked = false,
                isBreaking = false,
                readCount = 670,
                readingTimeMinutes = 3,
                author = "قسم الابتكار"
            ),

            // --- ECONOMY ---
            NewsArticleEntity(
                id = 7,
                title = "ارتفاع قياسي في أسعار الذهب والأسواق تترقب قرارات البنوك المركزية",
                summary = "المعدن الأصفر يلامس مستويات تاريخية جديدة مدفوعاً بزيادة الطلب الاستثماري والتحوط ضد التضخم وتقلبات العملات.",
                content = """سجلت أسعار الذهب مكاسب قوية في تعاملات اليوم بالبورصات العالمية، وسط توجه المستثمرين نحو الملاذات الآمنة والترقب الحذر لبيانات التضخم وقرارات أسعار الفائدة.

وتوقع محللون اقتصاديون استمرار الزخم الإيجابي لأسواق المعادن النفيسة مع تنامي احتياطيات البنوك المركزية عالمياً من الذهب خلال الربع الحالي.""",
                categoryId = NewsCategory.ECONOMY.id,
                sourceName = "الشرق الأوسط",
                sourceUrl = "https://aawsat.com",
                imageUrl = "https://images.unsplash.com/photo-1610375461246-83df859d849d?w=800&q=80",
                localDrawableResId = R.drawable.news_world_banner_1787575074316,
                publishedTimestamp = now - 1 * hour,
                isBookmarked = false,
                isBreaking = true,
                readCount = 780,
                readingTimeMinutes = 2,
                author = "التحليل الاقتصادي"
            ),
            NewsArticleEntity(
                id = 8,
                title = "طفرة في الاستثمارات الناشئة بمجال التكنولوجيا المالية في المنطقة",
                summary = "جولات تمويلية تتجاوز ملياري دولار لشركات الدفع الرقمي والحلول المصرفية السحابية المبتكرة.",
                content = """كشف تقرير مالي حديث عن نمو متسارع في حجم الصفقات الاستثمارية الموجهة للشركات الناشئة في قطاع التكنولوجيا المالية (FinTech).

ويعكس هذا الإقبال الكبير ثقة الصناديق العالمية في البنية الرقمية المتقدمة والبيئة التشريعية المحفزة للابتكار وريادة الأعمال.""",
                categoryId = NewsCategory.ECONOMY.id,
                sourceName = "العربية",
                sourceUrl = "https://www.alarabiya.net",
                imageUrl = "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?w=800&q=80",
                localDrawableResId = R.drawable.news_world_banner_1787575074316,
                publishedTimestamp = now - 5 * hour,
                isBookmarked = false,
                isBreaking = false,
                readCount = 390,
                readingTimeMinutes = 3,
                author = "أسواق المال"
            ),

            // --- HEALTH ---
            NewsArticleEntity(
                id = 9,
                title = "دراسة طبية حديثة: النوم المنتظم وتناول مضادات الأكسدة يجددان خلايا الدماغ",
                summary = "أبحاث علمية موثقة تكشف آليات الحفاظ على الذاكرة والوقاية من التوتر العصبي عبر عادات يومية بسيطة.",
                content = """أظهرت دراسة سريرية موسعة نشرتها إحدى الدوريات الطبية العالمية أن الحصول على 7 إلى 8 ساعات من النوم العميق ليلاً يسهم بشكل مباشر في تنشيط الجهاز الغليمفاوي المسؤول عن تنظيف خلايا الدماغ من السموم المتراكمة.

وأوصى الأطباء بالحرص على النشاط البدني المعتدل وتناول الخضروات الورقية والفواكه الغنية بمضادات الأكسدة للحفاظ على حيوية الجهاز العصبي.""",
                categoryId = NewsCategory.HEALTH.id,
                sourceName = "صحتك دوت كوم",
                sourceUrl = "https://www.sehatok.com",
                imageUrl = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800&q=80",
                localDrawableResId = R.drawable.news_world_banner_1787575074316,
                publishedTimestamp = now - 2 * hour,
                isBookmarked = false,
                isBreaking = false,
                readCount = 920,
                readingTimeMinutes = 3,
                author = "استشاري الطب الوقائي"
            ),
            NewsArticleEntity(
                id = 10,
                title = "اكتشاف جزيء طبيعي واعد يعزز المناعة ويحمي من الفيروسات الموسمية",
                summary = "تجارب مخبرية واعدة تفتح الباب لتطوير علاجات وقائية آمنة وفعالة دون آثار جانبية.",
                content = """نجح فريق بحثي دولي في عزل مركب طبيعي مستخلص من النباتات الطبية أثبت فاعلية عالية في تعزيز استجابة الأجسام المضادة للفيروسات التنفسية.

ويأمل الباحثون في بدء التجارب السريرية الأولية قريباً لإنتاج مكملات وقائية تدعم الجهاز المناعي لجميع الفئات العمرية.""",
                categoryId = NewsCategory.HEALTH.id,
                sourceName = "BBC عربي",
                sourceUrl = "https://www.bbc.com/arabic",
                imageUrl = "https://images.unsplash.com/photo-1532938911079-1b06ac7ceec7?w=800&q=80",
                localDrawableResId = R.drawable.news_world_banner_1787575074316,
                publishedTimestamp = now - 6 * hour,
                isBookmarked = false,
                isBreaking = false,
                readCount = 540,
                readingTimeMinutes = 2,
                author = "محرر الشؤون الصحية"
            ),

            // --- CULTURE & LIFESTYLE ---
            NewsArticleEntity(
                id = 11,
                title = "افتتاح معرض الفنون والحضارات الإنسانية بمشاركة مئات الفنانين العالميين",
                summary = "لوحات ومنحوتات ومجسمات رقمية تفاعلية تعكس التنوع الثقافي والتراث العريق برؤية معاصرة ملهمة.",
                content = """افتتح رسمياً المعرض الدولي للفنون التشكيلية والمعاصرة وسط إقبال جماهيري حاشد من المثقفين وعشاق الفن.

ويتضمن المعرض أجنحة تفاعلية تستخدم تقنيات الواقع المعزز لشرح القصص التاريخية للقطع الأثرية، بالإضافة إلى ورش عمل حيّة يقدمها فنانون عالميون.""",
                categoryId = NewsCategory.CULTURE.id,
                sourceName = "الجزيرة نت",
                sourceUrl = "https://www.aljazeera.net",
                imageUrl = "https://images.unsplash.com/photo-1499781350541-7783f6c6a0c8?w=800&q=80",
                localDrawableResId = R.drawable.news_world_banner_1787575074316,
                publishedTimestamp = now - 4 * hour,
                isBookmarked = false,
                isBreaking = false,
                readCount = 310,
                readingTimeMinutes = 3,
                author = "القسم الثقافي"
            ),
            NewsArticleEntity(
                id = 12,
                title = "اكتشاف مدينة أثرية مفقودة تعود لآلاف السنين تحت الرمال",
                summary = "علماء الآثار يعثرون على معابد ونقوش مسمارية تاريخية تسلط الضوء على فترات ازدهار الحضارات القديمة.",
                content = """أعلنت بعثة التنقيب الأثري عن اكتشاف استثنائي لمدينة أثرية متكاملة كانت مدفونة تحت الرمال لعدة قرون.

وتحتوي المدينة على مبانٍ سكنية وقنوات مائية متطورة ونقوش صخرية نادرة تقدم معلومات قيمة للمؤرخين حول أنماط الحياة والتجارة في تلك الحقبة التاريخية الغابرة.""",
                categoryId = NewsCategory.CULTURE.id,
                sourceName = "سكاي نيوز عربية",
                sourceUrl = "https://www.skynewsarabia.com",
                imageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?w=800&q=80",
                localDrawableResId = R.drawable.news_world_banner_1787575074316,
                publishedTimestamp = now - 7 * hour,
                isBookmarked = false,
                isBreaking = false,
                readCount = 1100,
                readingTimeMinutes = 4,
                author = "مراسل الآثار والتراث"
            )
        )
    }
}
