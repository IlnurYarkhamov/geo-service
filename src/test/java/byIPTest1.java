import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class byIPTest1 {
    @Mock
    GeoService gService;
    @Mock
    Location location; //что здесь происходит?
    @Mock
    LocalizationService localServ;
    @Test
    public void test1() {
        //Задание I
        location = mock(Location.class); //зачем нам здесь метод mock()? Что мы тут делаем?
        when(location.getCountry()).thenReturn(Country.valueOf("USA"));
        gService = mock(GeoService.class);
        when(gService.byIp(any())).thenReturn(location);
        localServ = mock(LocalizationService.class);
        when(localServ.locale(location.getCountry())).thenReturn("RUSSIA");

        MessageSenderImpl mSenderIml = new MessageSenderImpl(gService,localServ); //надо давать в конструктор объекты,
        //а мы не знаем, что в них, они сложные, поэтому нужен mock
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("1", "2");
//        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.123.12.19");
//        System.out.println(mSenderIml.send(headers));
        assertEquals("RUSSIA",mSenderIml.send(headers));

        //Задание II
        when(localServ.locale(location.getCountry())).thenReturn("USA");
        MessageSenderImpl mSenderIml2 = new MessageSenderImpl(gService,localServ);
        Map<String, String> headers2 = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96");
//        mSenderIml2.send(headers2);
        assertEquals("USA",mSenderIml.send(headers));
    }
    @Test
    public void test2() { //почему здесь не нужен Mock? Параметров никаких не нужно
        GeoServiceImpl gServiceImpl = new GeoServiceImpl();
        assertEquals(Country.RUSSIA, gServiceImpl.byIp("172.").getCountry());
//        System.out.println(gServiceImpl.byIp("172.").getCountry());
//        System.out.println(gServiceImpl.byIp("96.").getCountry());
        assertEquals(Country.USA, gServiceImpl.byIp("96.").getCountry());
    }
    @Test
    public void test3() {
        LocalizationServiceImpl localServImpl = new LocalizationServiceImpl();
//        System.out.println(localServImpl.locale(Country.RUSSIA));
//        System.out.println(localServImpl.locale(Country.USA));
        assertEquals("Добро пожаловать", localServImpl.locale(Country.RUSSIA));
        assertEquals("Welcome", localServImpl.locale(Country.USA));


    }
//    @Test //с заглушкой
//    public void test4() {
//        LocalizationServiceImpl localServImpl2 = mock(LocalizationServiceImpl.class);
//        when(localServImpl2.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");
//        System.out.println(localServImpl2.locale(Country.RUSSIA));
//    }
}
