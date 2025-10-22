// google-translate.js

function googleTranslateElementInit() {
  new google.translate.TranslateElement({
    pageLanguage: 'ja',
    includedLanguages: 'ar,yue,zh-CN,zh-TW,nl,fr,de,id,it,ko,pl,ru,es,th,vi,hi,bho,nr',
    layout: google.translate.TranslateElement.InlineLayout.SIMPLE
  }, 'google_translate_element');
}

// クッキーから言語を取得
function getLanguageCookie() {
  const match = document.cookie.match(/googtrans=\/ja\/([a-zA-Z\-]+)/);
  return match ? match[1] : null;
}

// ページロード時にクッキーの言語を適用
window.addEventListener('load', function() {
  const lang = getLanguageCookie();
  if(lang && lang !== 'ja') {
    document.cookie = "googtrans=/ja/" + lang + ";path=/";
  }
  googleTranslateElementInit();
});
