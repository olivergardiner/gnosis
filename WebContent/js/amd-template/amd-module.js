// AMD support
(function (factory) {
    "use strict";
    if (typeof define === 'function' && define.amd) {
        // using AMD; register as anon module
        define(['jQuery'], factory);
    } else {
        // no AMD; invoke directly
        factory(jQuery);
    }
}

(function($) {
	"use strict";
	// Declare module here

}));