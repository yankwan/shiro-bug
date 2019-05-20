package com.shiro.samples;

import com.shiro.samples.web.utils.Base64Tools;

public class TestDemo {

    public static void main(String[] args) {
        String rememberMe = "EnPsbl6p7DiLBENtrnYRfY9IU+OqK5G2G+sJ2DRtyoQo3sQxpzQMPzBeVOEm6h8d+4E1vm3sI6FTd8Ru6KNFSccX4fXm1FQeVie8Yw8VqGcUSLE8HMYzpI9/Pnt4FTgXmR94LkQzhy48X+4Dy7J+oBm/Kf6N/L2ynNxYUd/wHjrdG+dlMQp5r13O4FilF1vX5yQ2BPAAik3RX/kxiVUQRnWykK8Gza+jdHjP6j4nUAQuMcwXlE2o6rxrLrfyP2J2afTUFfsJHt6W/Hjh2qLHoEEtBN7QjvY2vSobLVzPOcNec6G3zvUBRFaY+9T5JdDiNDC+lnan/aUl8yEZ3FDGB5OZjaia5ELRjh/8TfR5jTMQls7zbbntt13Sz/Wdd10jU67tbXkBNYbS42mDIV0jBxiyZi72y6ZuHDbxu6LhtRy/JU1+lAlnvSaNCjY6sIiQZo3ptYCQeTUxJGkk/Xe+vYj/1y5+lmaMZZdDt3LHaQbqk8XoqMRgLPMBtbyq1ITt";
        Base64Tools.decoderBase64File(rememberMe, "rememberMe");
    }
}
