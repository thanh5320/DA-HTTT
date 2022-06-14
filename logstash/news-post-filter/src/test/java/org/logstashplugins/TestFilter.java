package org.logstashplugins;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Event;
import co.elastic.logstash.api.FilterMatchListener;
import org.junit.Test;
import org.logstash.plugins.ConfigurationImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFilter {

    @Test
    public void test1()  {
        String sourceField = "message";
        Configuration config = new ConfigurationImpl(Collections.singletonMap("source", sourceField));
        NewsPostFilter filter = new NewsPostFilter("test-id", config, null);

        Event e = new org.logstash.Event();
        TestMatchListener matchListener = new TestMatchListener();
        e.setField(sourceField, "{\"raw_url\": \"https://thegioihoinhap.vn/tieu-chuan-va-hoi-nhap/viet-nam-la-thi-truong-xuat-khau-rau-qua-da-qua-che-bien-lon-thu-9-toan-cau/\", \"domain\": \"thegioihoinhap.vn\", \"title\": \"Vi\\u1ec7t Nam l\\u00e0 th\\u1ecb tr\\u01b0\\u1eddng xu\\u1ea5t kh\\u1ea9u rau qu\\u1ea3 \\u0111\\u00e3 qua ch\\u1ebf bi\\u1ebfn l\\u1edbn th\\u1ee9 9 to\\u00e0n c\\u1ea7u\", \"content\": \"Nh\\u1eefng n\\u0103m g\\u1ea7n \\u0111\\u00e2y, Vi\\u1ec7t Nam \\u0111\\u00e3 ph\\u00e1t tri\\u1ec3n ng\\u00e0nh c\\u00f4ng nghi\\u1ec7p ch\\u1ebf bi\\u1ebfn tr\\u00e1i c\\u00e2y, hi\\u1ec7n chi\\u1ebfm kho\\u1ea3ng 10% t\\u1ed5ng s\\u1ea3n l\\u01b0\\u1ee3ng tr\\u00e1i c\\u00e2y c\\u1ea3 n\\u01b0\\u1edbc.Theo s\\u1ed1 li\\u1ec7u th\\u1ed1ng k\\u00ea t\\u1eeb T\\u1ed5ng c\\u1ee5c H\\u1ea3i quan, giai \\u0111o\\u1ea1n 2016 \\u2013 2020, t\\u1ed1c \\u0111\\u1ed9 xu\\u1ea5t kh\\u1ea9u h\\u00e0ng rau qu\\u1ea3 \\u0111\\u00e3 qua ch\\u1ebf bi\\u1ebfn c\\u1ee7a Vi\\u1ec7t Nam \\u0111\\u1ec1u t\\u0103ng tr\\u01b0\\u1edfng \\u1edf m\\u1ee9c 2 con s\\u1ed1. \\u0110\\u00e1ng ch\\u00fa \\u00fd, t\\u1ed1c \\u0111\\u1ed9 xu\\u1ea5t kh\\u1ea9u rau qu\\u1ea3 ch\\u1ebf bi\\u1ebfn c\\u1ee7a Vi\\u1ec7t Nam n\\u0103m 2019 t\\u0103ng t\\u1edbi 41,2% so v\\u1edbi n\\u0103m 2018, nh\\u01b0ng sau \\u0111\\u00f3 c\\u00f3 d\\u1ea5u hi\\u1ec7u ch\\u1eadm l\\u1ea1i.N\\u0103m 2020, t\\u1ed1c \\u0111\\u1ed9 xu\\u1ea5t kh\\u1ea9u h\\u00e0ng rau qu\\u1ea3 ch\\u1ebf bi\\u1ebfn c\\u1ee7a Vi\\u1ec7t Nam t\\u0103ng 11,1% so v\\u1edbi n\\u0103m 2019. Trong 9 th\\u00e1ng \\u0111\\u1ea7u n\\u0103m 2021, do kh\\u00f3 kh\\u0103n trong kh\\u00e2u v\\u1eadn chuy\\u1ec3n, trong khi v\\u1ea5n \\u0111\\u1ec1 b\\u1ea3o qu\\u1ea3n h\\u00e0ng rau qu\\u1ea3 t\\u01b0\\u01a1i c\\u1ee7a Vi\\u1ec7t Nam c\\u00f2n h\\u1ea1n ch\\u1ebf, do \\u0111\\u00f3, doanh nghi\\u1ec7p c\\u00f3 s\\u1ef1 chuy\\u1ec3n d\\u1ecbch kh\\u00e1 th\\u00e0nh c\\u00f4ng khi \\u0111\\u1ea9y m\\u1ea1nh xu\\u1ea5t kh\\u1ea9u s\\u1ea3n ph\\u1ea9m ch\\u1ebf bi\\u1ebfn.Theo \\u0111\\u00f3, kim ng\\u1ea1ch xu\\u1ea5t kh\\u1ea9u h\\u00e0ng rau qu\\u1ea3 \\u0111\\u00e3 qua ch\\u1ebf bi\\u1ebfn th\\u00e1ng 9/2021 \\u0111\\u1ea1t 65,42 tri\\u1ec7u USD, t\\u0103ng 13,7% so v\\u1edbi th\\u00e1ng 9/2020. T\\u00ednh chung 9 th\\u00e1ng \\u0111\\u1ea7u n\\u0103m 2021, kim ng\\u1ea1ch xu\\u1ea5t kh\\u1ea9u h\\u00e0ng rau qu\\u1ea3 \\u0111\\u00e3 qua ch\\u1ebf bi\\u1ebfn \\u0111\\u1ea1t 653,5 tri\\u1ec7u USD, t\\u0103ng 16% so v\\u1edbi c\\u00f9ng k\\u1ef3 n\\u0103m 2020. C\\u00e1c th\\u1ecb tr\\u01b0\\u1eddng xu\\u1ea5t kh\\u1ea9u ch\\u00ednh m\\u1eb7t h\\u00e0ng rau qu\\u1ea3 ch\\u1ebf bi\\u1ebfn c\\u1ee7a Vi\\u1ec7t Nam g\\u1ed3m: Trung Qu\\u1ed1c, M\\u1ef9, Nh\\u1eadt B\\u1ea3n, H\\u00e0n Qu\\u1ed1c, Nga, Australia \\u2026D\\u1ef1 b\\u00e1o trong nh\\u1eefng th\\u00e1ng cu\\u1ed1i n\\u0103m 2021 v\\u00e0 c\\u1ea3 n\\u0103m 2022, rau qu\\u1ea3 ch\\u1ebf bi\\u1ebfn v\\u1eabn s\\u1ebd l\\u00e0 ch\\u1ee7ng lo\\u1ea1i s\\u1ea3n ph\\u1ea9m c\\u00f3 ti\\u1ec1m n\\u0103ng t\\u0103ng tr\\u01b0\\u1edfng b\\u1edfi s\\u1ef1 ti\\u1ec7n l\\u1ee3i v\\u00e0 th\\u1eddi gian b\\u1ea3o qu\\u1ea3n l\\u00e2u.Theo th\\u00f4ng tin t\\u1eeb https://www.gminsights.com, th\\u1ecb tr\\u01b0\\u1eddng rau qu\\u1ea3 ch\\u1ebf bi\\u1ebfn to\\u00e0n c\\u1ea7u d\\u1ef1 ki\\u1ebfn \\u0111\\u1ea1t t\\u1ed1c \\u0111\\u1ed9 t\\u0103ng tr\\u01b0\\u1edfng b\\u00ecnh qu\\u00e2n 7%/n\\u0103m trong giai \\u0111o\\u1ea1n 2020 \\u2013 2027. Do l\\u1ed1i s\\u1ed1ng b\\u1eadn r\\u1ed9n, ng\\u01b0\\u1eddi ti\\u00eau d\\u00f9ng xem c\\u00e1c s\\u1ea3n ph\\u1ea9m tr\\u00e1i c\\u00e2y v\\u00e0 rau c\\u1ee7 \\u0111\\u00e3 qua ch\\u1ebf bi\\u1ebfn nh\\u01b0 m\\u1ed9t gi\\u1ea3i ph\\u00e1p ti\\u1ebft ki\\u1ec7m th\\u1eddi gian m\\u00e0 v\\u1eabn c\\u00f3 ngu\\u1ed3n n\\u0103ng l\\u01b0\\u1ee3ng b\\u1ed5 sung cho c\\u01a1 th\\u1ec3.Trung t\\u00e2m th\\u00f4ng tin c\\u00f4ng nghi\\u1ec7p v\\u00e0 th\\u01b0\\u01a1ng m\\u1ea1i (B\\u1ed9 C\\u00f4ng Th\\u01b0\\u01a1ng) cho bi\\u1ebft, c\\u00e1c doanh nghi\\u1ec7p \\u0111\\u1ea7u t\\u01b0 v\\u00e0o l\\u0129nh v\\u1ef1c ch\\u1ebf bi\\u1ebfn b\\u1ea3o qu\\u1ea3n tr\\u00e1i c\\u00e2y c\\u1ee7a Vi\\u1ec7t Nam nh\\u1eefng n\\u0103m g\\u1ea7n \\u0111\\u00e2y t\\u0103ng m\\u1ea1nh, g\\u1ea5p 3 l\\u1ea7n so v\\u1edbi tr\\u01b0\\u1edbc \\u0111\\u00f3, v\\u1edbi 7.500 c\\u01a1 s\\u1edf ch\\u1ebf bi\\u1ebfn b\\u1ea3o qu\\u1ea3n tr\\u00e1i c\\u00e2y, rau c\\u1ee7 v\\u00e0 kho\\u1ea3ng 156 nh\\u00e0 m\\u00e1y ch\\u1ebf bi\\u1ebfn c\\u00f3 d\\u00e2y chuy\\u1ec1n, c\\u00f4ng ngh\\u1ec7 hi\\u1ec7n \\u0111\\u1ea1i.Nh\\u01b0ng tr\\u00ean th\\u1ef1c t\\u1ebf, ng\\u00e0nh ch\\u1ebf bi\\u1ebfn ch\\u1ec9 m\\u1edbi \\u0111\\u00e1p \\u1ee9ng s\\u01a1 ch\\u1ebf 8 \\u2013 10% s\\u1ea3n l\\u01b0\\u1ee3ng rau qu\\u1ea3 s\\u1ea3n xu\\u1ea5t ra h\\u00e0ng n\\u0103m. \\u0110\\u1ebfn nay, 76,2% rau qu\\u1ea3 xu\\u1ea5t kh\\u1ea9u ch\\u01b0a qua ch\\u1ebf bi\\u1ebfn; vi\\u1ec7c ti\\u00eau th\\u1ee5 v\\u1eabn \\u1edf d\\u1ea1ng t\\u01b0\\u01a1i ho\\u1eb7c s\\u01a1 ch\\u1ebf b\\u1ea3o qu\\u1ea3n l\\u00e0 ch\\u1ee7 y\\u1ebfu, t\\u1ed5n th\\u1ea5t sau thu ho\\u1ea1ch c\\u00f2n qu\\u00e1 cao kho\\u1ea3ng tr\\u00ean 20%.Nh\\u01b0 v\\u1eady c\\u00f2n r\\u1ea5t nhi\\u1ec1u d\\u01b0 \\u0111\\u1ecba ph\\u00e1t tri\\u1ec3n cho ng\\u00e0nh ch\\u1ebf bi\\u1ebfn rau qu\\u1ea3 xu\\u1ea5t kh\\u1ea9u trong th\\u1eddi gian t\\u1edbi.\", \"author_display_name\": null, \"first_crawled_time\": \"2021/11/17 08:39:14\", \"published_time\": \"2021/11/15 09:33:40\", \"summary\": \"Vi\\u1ec7t Nam l\\u00e0 th\\u1ecb tr\\u01b0\\u1eddng xu\\u1ea5t kh\\u1ea9u ch\\u1ebf ph\\u1ea9m t\\u1eeb rau, tr\\u00e1i c\\u00e2y, qu\\u1ea3 h\\u1ea1ch ho\\u1eb7c c\\u00e1c b\\u1ed9 ph\\u1eadn kh\\u00e1c c\\u1ee7a th\\u1ef1c v\\u1eadt l\\u1edbn th\\u1ee9 9 to\\u00e0n c\\u1ea7u, t\\u1ed1c \\u0111\\u1ed9 xu\\u1ea5t kh\\u1ea9u m\\u1eb7t h\\u00e0ng n\\u00e0y c\\u1ee7a Vi\\u1ec7t Nam ra th\\u1ecb tr\\u01b0\\u1eddng th\\u1ebf gi\\u1edbi trong giai \\u0111o\\u1ea1n 2016 \\u2013 2020 t\\u0103ng tr\\u01b0\\u1edfng b\\u00ecnh qu\\u00e2n 22,15%/n\\u0103m.\", \"image_sources\": [\"https://thegioihoinhap.vn/data/uploads/2021/11/rauquachebien_bfei.png\"], \"video_sources\": [\"https://www.youtube.com/embed/qeyvXOHO5_s?feature=oembed\"], \"tag\": [\"xu\\u1ea5t kh\\u1ea9u rau qu\\u1ea3\"], \"share_content\": []}");
        Collection<Event> results = filter.filter(Collections.singletonList(e), matchListener);

        assertEquals(1, results.size());
        Iterator<Event> ei =  results.iterator();
        while(ei.hasNext()){
            System.out.println(ei.next().getField("message"));
        }
    }
}

class TestMatchListener implements FilterMatchListener {

    private AtomicInteger matchCount = new AtomicInteger(0);

    @Override
    public void filterMatched(Event event) {
        matchCount.incrementAndGet();
    }

    public int getMatchCount() {
        return matchCount.get();
    }
}