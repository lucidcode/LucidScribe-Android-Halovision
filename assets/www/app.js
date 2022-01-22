/*
 * File: app.js
 *
 * This file was generated by Sencha Architect version 2.2.3.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Sencha Touch 2.2.x library, under independent license.
 * License of Sencha Architect does not include license for Sencha Touch 2.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

//@require @packageOverrides
Ext.Loader.setConfig({

});

Ext.application({
    models: [
        'AccelerometerModel',
        'SettingsModel',
        'ColorModel',
        'AlgorithmModel',
        'AudioTrackModel',
        'AdvancedModel'
    ],
    stores: [
        'AccelerometerStore',
        'SettingsStore',
        'ColorStore',
        'AlgorithmStore',
        'AudioTrackStore',
        'AdvancedStore'
    ],
    views: [
        'SettingsPanel',
        'MainPanel',
        'CurrentPanel',
        'AdvancedPanel'
    ],
    controllers: [
        'MainController'
    ],
    name: 'Halovision',

    launch: function() {
        console.log ('Launching Halovision');

        //Hide the splash screen if running with Cordova
        if (navigator.splashscreen !== undefined) {    
            setTimeout(function() {
                console.log('Hiding Splash Screen');
                navigator.splashscreen.hide();		    
            }, 2000);
        }
        Ext.create('Halovision.view.MainPanel', {fullscreen: true});
    }

});