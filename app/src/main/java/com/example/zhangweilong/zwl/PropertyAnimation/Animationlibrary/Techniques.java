
/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 daimajia
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary;


import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.attention.BounceAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.attention.FlashAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.attention.PulseAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.attention.RubberBandAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.attention.ShakeAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.attention.StandUpAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.attention.SwingAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.attention.TadaAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.attention.WaveAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.attention.WobbleAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.bouncing_entrances.BounceInAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.bouncing_entrances.BounceInDownAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.bouncing_entrances.BounceInLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.bouncing_entrances.BounceInRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.bouncing_entrances.BounceInUpAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.fading_entrances.FadeInAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.fading_entrances.FadeInDownAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.fading_entrances.FadeInLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.fading_entrances.FadeInRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.fading_entrances.FadeInUpAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.fading_exits.FadeOutAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.fading_exits.FadeOutDownAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.fading_exits.FadeOutLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.fading_exits.FadeOutRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.fading_exits.FadeOutUpAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.flippers.FlipInXAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.flippers.FlipInYAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.flippers.FlipOutXAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.flippers.FlipOutYAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.rotating_entrances.RotateInAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.rotating_entrances.RotateInDownLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.rotating_entrances.RotateInDownRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.rotating_entrances.RotateInUpLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.rotating_entrances.RotateInUpRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.rotating_exits.RotateOutAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.rotating_exits.RotateOutDownLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.rotating_exits.RotateOutDownRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.rotating_exits.RotateOutUpLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.rotating_exits.RotateOutUpRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.sliders.SlideInDownAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.sliders.SlideInLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.sliders.SlideInRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.sliders.SlideInUpAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.sliders.SlideOutDownAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.sliders.SlideOutLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.sliders.SlideOutRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.sliders.SlideOutUpAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.zooming_entrances.ZoomInAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.zooming_entrances.ZoomInDownAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.zooming_entrances.ZoomInLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.zooming_entrances.ZoomInRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.zooming_entrances.ZoomInUpAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.zooming_exits.ZoomOutAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.zooming_exits.ZoomOutDownAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.zooming_exits.ZoomOutLeftAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.zooming_exits.ZoomOutRightAnimator;
import com.example.zhangweilong.zwl.PropertyAnimation.Animationlibrary.zooming_exits.ZoomOutUpAnimator;

public enum Techniques {

    Flash(FlashAnimator.class),
    Pulse(PulseAnimator.class),
    RubberBand(RubberBandAnimator.class),
    Shake(ShakeAnimator.class),
    Swing(SwingAnimator.class),
    Wobble(WobbleAnimator.class),
    Bounce(BounceAnimator.class),
    Tada(TadaAnimator.class),
    StandUp(StandUpAnimator.class),
    Wave(WaveAnimator.class),

    BounceIn(BounceInAnimator.class),
    BounceInDown(BounceInDownAnimator.class),
    BounceInLeft(BounceInLeftAnimator.class),
    BounceInRight(BounceInRightAnimator.class),
    BounceInUp(BounceInUpAnimator.class),

    FadeIn(FadeInAnimator.class),
    FadeInUp(FadeInUpAnimator.class),
    FadeInDown(FadeInDownAnimator.class),
    FadeInLeft(FadeInLeftAnimator.class),
    FadeInRight(FadeInRightAnimator.class),

    FadeOut(FadeOutAnimator.class),
    FadeOutDown(FadeOutDownAnimator.class),
    FadeOutLeft(FadeOutLeftAnimator.class),
    FadeOutRight(FadeOutRightAnimator.class),
    FadeOutUp(FadeOutUpAnimator.class),

    FlipInX(FlipInXAnimator.class),
    FlipOutX(FlipOutXAnimator.class),
    FlipInY(FlipInYAnimator.class),
    FlipOutY(FlipOutYAnimator.class),
    RotateIn(RotateInAnimator.class),
    RotateInDownLeft(RotateInDownLeftAnimator.class),
    RotateInDownRight(RotateInDownRightAnimator.class),
    RotateInUpLeft(RotateInUpLeftAnimator.class),
    RotateInUpRight(RotateInUpRightAnimator.class),

    RotateOut(RotateOutAnimator.class),
    RotateOutDownLeft(RotateOutDownLeftAnimator.class),
    RotateOutDownRight(RotateOutDownRightAnimator.class),
    RotateOutUpLeft(RotateOutUpLeftAnimator.class),
    RotateOutUpRight(RotateOutUpRightAnimator.class),

    SlideInLeft(SlideInLeftAnimator.class),
    SlideInRight(SlideInRightAnimator.class),
    SlideInUp(SlideInUpAnimator.class),
    SlideInDown(SlideInDownAnimator.class),

    SlideOutLeft(SlideOutLeftAnimator.class),
    SlideOutRight(SlideOutRightAnimator.class),
    SlideOutUp(SlideOutUpAnimator.class),
    SlideOutDown(SlideOutDownAnimator.class),

    ZoomIn(ZoomInAnimator.class),
    ZoomInDown(ZoomInDownAnimator.class),
    ZoomInLeft(ZoomInLeftAnimator.class),
    ZoomInRight(ZoomInRightAnimator.class),
    ZoomInUp(ZoomInUpAnimator.class),

    ZoomOut(ZoomOutAnimator.class),
    ZoomOutDown(ZoomOutDownAnimator.class),
    ZoomOutLeft(ZoomOutLeftAnimator.class),
    ZoomOutRight(ZoomOutRightAnimator.class),
    ZoomOutUp(ZoomOutUpAnimator.class);



    private Class animatorClazz;

    private Techniques(Class clazz) {
        animatorClazz = clazz;
    }

    public BaseViewAnimator getAnimator() {
        try {
            return (BaseViewAnimator) animatorClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
