package org.mycompany;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyTranslationServiceTest_TODO {
    @Mock
    private Translate translate;

    @Mock
    private Translation translation;

    /**
     * 1. Happy case test.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is equal to "ru",
     * `googleTranslate` dependency should be called and `translation.getTranslatedText()` returned.
     * No other interactions with `googleTranslate` dependency should be invoked apart from a single call to `googleTranslate.translate()`.
     */
    @Test
    void translateWithGoogle_anySentenceAndTargetLanguageIsRu_success() {
        MyTranslationService myTranslationService = new MyTranslationService(translate);
        String targetSentence = "Any sentence";
        String translatedSentence = "Любое предложение";
        String targetLanguage = "ru";

        when(translate.translate(eq(targetSentence), any())).thenReturn(translation);
        when(translation.getTranslatedText()).thenReturn(translatedSentence);

        String resultSentence = myTranslationService.translateWithGoogle(targetSentence, targetLanguage);
        assertEquals(translatedSentence, resultSentence);
        verify(translate, times(1)).translate(eq(targetSentence), any());
        verify(translation, times(1)).getTranslatedText();

    }

    /**
     * 2. Unhappy case test when target language is not supported.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is not equal to "ru",
     * `IllegalArgumentException` should be thrown. `googleTranslate` dependency should not be called at all.
     */
    @Test
    void translateWithGoogle_anySentenceAndTargetLanguageIsNotRu_failure() {
        MyTranslationService myTranslationService = new MyTranslationService(translate);
        String targetSentence = "Any sentence";
        String targetLanguage = "en";

        assertThrows(
                IllegalArgumentException.class,
                () -> myTranslationService.translateWithGoogle(targetSentence, targetLanguage)
        );
        verifyNoInteractions(translate);
        verifyNoInteractions(translation);
    }

    /**
     * 3. Unhappy case test when Google Translate call throws exception.
     * <p>
     * When `MyTranslationService::translateWithGoogle` method is called with any sentence and target language is equal to "ru",
     * `googleTranslate` dependency should be called. When `googleTranslate` dependency throws exception, it should be
     * wrapped into `MyTranslationServiceException` and the latter should be thrown from our method.
     */
    @Test
    void translateWithGoogle_googleTranslateThrowsException_failure() {
        MyTranslationService myTranslationService = new MyTranslationService(translate);
        String targetSentence = "Any sentence";
        String targetLanguage = "ru";
        when(translate.translate(eq(targetSentence), any())).thenThrow(new RuntimeException());

        assertThrows(
                MyTranslationServiceException.class,
                () -> myTranslationService.translateWithGoogle(targetSentence, targetLanguage)
        );
        verify(translate, times(1)).translate(eq(targetSentence), any());
        verifyNoInteractions(translation);
    }
}