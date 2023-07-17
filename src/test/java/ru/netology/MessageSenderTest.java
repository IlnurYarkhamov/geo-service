package ru.netology;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageSenderTest {
    @Test
    public void testChecklanguages() {
        //Задание. Проверка отправки только русского текста, если ip российский
        Location location = mock(Location.class);
        when(location.getCountry()).thenReturn(Country.valueOf("USA"));
        GeoService gService = mock(GeoService.class);
        when(gService.byIp(any())).thenReturn(location);
        LocalizationService localServ = mock(LocalizationService.class);
        when(localServ.locale(location.getCountry())).thenReturn("RUSSIA");

        MessageSenderImpl mSenderIml = new MessageSenderImpl(gService,localServ);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("1", "2");
        assertEquals("RUSSIA",mSenderIml.send(headers));

        //Задание. Проверка отправки только английского текста, если ip американский
        when(localServ.locale(location.getCountry())).thenReturn("USA");
        MessageSenderImpl mSenderIml2 = new MessageSenderImpl(gService,localServ);
        Map<String, String> headers2 = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96");
        assertEquals("USA",mSenderIml.send(headers));
    }
    @Test
    public void testCheckLocation() {
        GeoServiceImpl gServiceImpl = new GeoServiceImpl();
        assertEquals(Country.RUSSIA, gServiceImpl.byIp("172.").getCountry());
        assertEquals(Country.USA, gServiceImpl.byIp("96.").getCountry());
    }
    @Test
    public void testCheckReturnMessage() {
        LocalizationServiceImpl localServImpl = new LocalizationServiceImpl();
        assertEquals("Добро пожаловать", localServImpl.locale(Country.RUSSIA));
        assertEquals("Welcome", localServImpl.locale(Country.USA));
    }
}

