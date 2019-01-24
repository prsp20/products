package com.prakass.products.converter;

import com.prakass.products.client.dto.ColorSwatchDto;
import com.prakass.products.client.dto.PriceDto;
import com.prakass.products.client.dto.ProductDto;
import com.prakass.products.domain.ColorSwatch;
import com.prakass.products.domain.PriceLabelType;
import com.prakass.products.domain.Product;
import com.prakass.products.util.NumberUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductDtoConverter {

    private static final Map<String, String> COLOR_MAP = new HashMap<>();
    private static final Map<String, String> CURRENTY_MAP = new HashMap<>();

    static {
        COLOR_MAP.put("Black", "#000000");
        COLOR_MAP.put("White", "#FFFFFF");
        COLOR_MAP.put("Red", "#FF0000");
        COLOR_MAP.put("Lime", "#00FF00");
        COLOR_MAP.put("Blue", "#0000FF");
        COLOR_MAP.put("Yellow", "#FFFF00");
        COLOR_MAP.put("Cyan", "#00FFFF");
        COLOR_MAP.put("Aqua", "#00FFFF");
        COLOR_MAP.put("Magenta", "#FF00FF");
        COLOR_MAP.put("Fuchsia", "#FF00FF");
        COLOR_MAP.put("Silver", "#C0C0C0");
        COLOR_MAP.put("Gray", "#808080");
        COLOR_MAP.put("Grey", "#808080");
        COLOR_MAP.put("Maroon", "#800000");
        COLOR_MAP.put("Olive", "#808000");
        COLOR_MAP.put("Green", "#008000");
        COLOR_MAP.put("Purple", "#800080");
        COLOR_MAP.put("Teal", "#008080");
        COLOR_MAP.put("Navy", "#000080");

        CURRENTY_MAP.put("GBP", "£");
        CURRENTY_MAP.put("USD", "$");
    }

    public Product convertProductDtoToProduct(ProductDto productDto, PriceLabelType priceLabelType) {
        List<ColorSwatch> colorSwatches = Optional.ofNullable(productDto.getColorSwatchDtoList())
                .orElse(new ArrayList<>()).stream().map(this::convertColorSwatch).collect(Collectors.toList());

        PriceDto priceDto = productDto.getPriceDto();
        return new Product(productDto.getProductId(), productDto.getTitle(), formatPrice(priceDto.getNowPrice(), priceDto.getCurrency()),
                getPriceLabel(priceDto, priceLabelType), colorSwatches);
    }

    private ColorSwatch convertColorSwatch(ColorSwatchDto colorSwatchDto) {
        String rgbColor = COLOR_MAP.get(colorSwatchDto.getBasicColor());
        rgbColor = rgbColor != null ? rgbColor : colorSwatchDto.getBasicColor();
        return new ColorSwatch(colorSwatchDto.getColor(), rgbColor, colorSwatchDto.getSkuId());
    }

    private String formatPrice(double price, String currency) {
        String currencySign = CURRENTY_MAP.get(currency);
        currencySign = currencySign != null ? currencySign : currency;
        if (price >= 10 && NumberUtil.isNumberAnInt(price)) {
            return String.format("%s%d", currencySign, (int) price);
        } else {
            return String.format("%s%.2f", currencySign, price);
        }
    }

    private String getPriceLabel(PriceDto priceDto, PriceLabelType type) {
        String priceLabel = "";
        String wasPrice = String.format("Was %s", formatPrice(priceDto.getWasPrice(), priceDto.getCurrency()));
        String nowPrice = String.format("now %s", formatPrice(priceDto.getNowPrice(), priceDto.getCurrency()));
        switch (type) {
            case ShowWasNow:
                priceLabel = String.format("%s, %s", wasPrice, nowPrice);
                break;
            case ShowWasThenNow:
                priceLabel = wasPrice;
                double thenPrice = getThenPrice(priceDto);
                if (thenPrice != 0) {
                    priceLabel = String.format("%s, then %s", priceLabel,
                            formatPrice(thenPrice, priceDto.getCurrency()));
                }
                priceLabel = String.format("%s, %s", priceLabel, nowPrice);
                break;
            case ShowPercDscount:
                priceLabel = String.format("%s off, %s", discountPercentage(priceDto), nowPrice);
                break;
        }

        return priceLabel;
    }

    private double getThenPrice(PriceDto priceDto) {
        if (priceDto.getThen2Price() != 0) {
            return priceDto.getThen2Price();
        } else if (priceDto.getThen1Price() != 0) {
            return priceDto.getThen1Price();
        }

        return 0;
    }

    private String discountPercentage(PriceDto priceDto) {
        return String.format("%d%%", (int) Math.floor(priceDto.getPriceReduction() / priceDto.getWasPrice() * 100));
    }
}
