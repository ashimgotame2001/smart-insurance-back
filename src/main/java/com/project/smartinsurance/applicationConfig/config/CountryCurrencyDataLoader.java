package com.project.smartinsurance.applicationConfig.config;

import com.project.smartinsurance.applicationConfig.model.Country;
import com.project.smartinsurance.applicationConfig.model.Currency;
import com.project.smartinsurance.applicationConfig.model.CurrencyFormat;
import com.project.smartinsurance.applicationConfig.repository.CountryRepository;
import com.project.smartinsurance.applicationConfig.repository.CurrencyFormatRepository;
import com.project.smartinsurance.applicationConfig.repository.CurrencyRepository;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class CountryCurrencyDataLoader implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final CurrencyFormatRepository currencyFormatRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (countryRepository.count() == 0) {
            List<Country> countries = List.of(
                createCountry("United States", "US"),
                createCountry("United Kingdom", "GB"),
                createCountry("European Union", "EU"),
                createCountry("Japan", "JP"),
                createCountry("Nepal", "NP"),
                createCountry("India", "IN"),
                createCountry("Australia", "AU"),
                createCountry("Canada", "CA"),
                createCountry("Switzerland", "CH"),
                createCountry("China", "CN")
            );
            countryRepository.saveAll(countries);

            List<Currency> currencies = List.of(
                createCurrency("US Dollar", "USD", "$", countries.get(0)),
                createCurrency("Pound Sterling", "GBP", "\u00A3", countries.get(1)),
                createCurrency("Euro", "EUR", "\u20AC", countries.get(2)),
                createCurrency("Japanese Yen", "JPY", "\u00A5", countries.get(3)),
                createCurrency("Nepalese Rupee", "NPR", "\u0930\u0942", countries.get(4)),
                createCurrency("Indian Rupee", "INR", "\u20B9", countries.get(5)),
                createCurrency("Australian Dollar", "AUD", "A$", countries.get(6)),
                createCurrency("Canadian Dollar", "CAD", "C$", countries.get(7)),
                createCurrency("Swiss Franc", "CHF", "Fr.", countries.get(8)),
                createCurrency("Chinese Yuan", "CNY", "\u00A5", countries.get(9))
            );
            currencyRepository.saveAll(currencies);
        }

        if (currencyFormatRepository.count() == 0) {
            List<CurrencyFormat> formats = List.of(
                createFormat("en-US", "US Standard", "1,234,567.89"),
                createFormat("en-IN", "Indian Style", "12,34,567.89"),
                createFormat("de-DE", "European", "1.234.567,89"),
                createFormat("en-GB", "British", "1,234,567.89"),
                createFormat("ja-JP", "Japanese", "1,234,567"),
                createFormat("en-AU", "Australian", "1,234,567.89"),
                createFormat("en-CA", "Canadian", "1,234,567.89"),
                createFormat("fr-CH", "Swiss", "1'234'567.89"),
                createFormat("zh-CN", "Chinese", "1,234,567.89"),
                createFormat("en-NG", "Nigerian", "1,234,567.89")
            );
            currencyFormatRepository.saveAll(formats);
        }
    }

    private Country createCountry(String name, String code) {
        Country country = new Country();
        country.setName(name);
        country.setCode(code);
        country.setStatus(Status.ACTIVE);
        return country;
    }

    private Currency createCurrency(String name, String code, String symbol, Country country) {
        Currency currency = new Currency();
        currency.setName(name);
        currency.setCode(code);
        currency.setSymbol(symbol);
        currency.setCountry(country);
        currency.setStatus(Status.ACTIVE);
        return currency;
    }

    private CurrencyFormat createFormat(String locale, String name, String description) {
        CurrencyFormat format = new CurrencyFormat();
        format.setLocale(locale);
        format.setName(name);
        format.setDescription(description);
        format.setStatus(Status.ACTIVE);
        return format;
    }
}
