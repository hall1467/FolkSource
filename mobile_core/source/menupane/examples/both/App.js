/* App.js */

enyo.kind({
  name: "App",
  classes: "enyo-unselectable",
  components: [
    // height necessary.
    { name: "menupane", kind: "MenuPane",
      style: "height: 600px; width: 360px;",
      onViewChanged: "viewChangedHandler",
      onMenuOpened: "menuOpenedHandler",
      onMenuClosed: "menuClosedHandler",
      onSecondaryMenuOpened: "secondaryMenuOpenedHandler",
      onSecondaryMenuClosed: "secondaryMenuClosedHandler",
      menu: [
        { content: "Primary Menu", classes: "menu-header" },
        { content: "A", view: "a", classes: "menu-item" },
        { view: "b", classes: "menu-item",
          components: [ { content: "B" }, { content: "1" } ] },
        { content: "More Header", classes: "menu-header" },
        { content: "C", view: "c", classes: "menu-item" },
        { content: "D", view: "d", classes: "menu-item" }
      ],
      secondarymenu: [
        { content: "Secondary Menu", classes: "menu-header" },
        { content: "E", view: "e", classes: "menu-item" },
        { content: "F", view: "f", classes: "menu-item" }
      ],
      components: [
        { name: "a", classes: "view",
          components: [
            { kind: "Toolbar",
              header: "A",
              onToggleMenu: "toolbarToggleMenuHandler",
              onToggleSecondaryMenu: "toolbarToggleSecondaryMenuHandler" },
            { content: "[View A]", classes: "content", style: "background-color: rgb(192,192,255);" } ]
        },
        { name: "b", classes: "view",
          components: [
            { kind: "Toolbar",
              onToggleMenu: "toolbarToggleMenuHandler",
              onToggleSecondaryMenu: "toolbarToggleSecondaryMenuHandler" },
            { classes: "content", style: "background-color: rgb(255,192,192);",
              components: [
                { content: "[View B]" },
                { content: "More" }
              ] }
          ]
        },
        { name: "c", classes: "view",
          components: [
            { kind: "Toolbar",
              onToggleMenu: "toolbarToggleMenuHandler",
              onToggleSecondaryMenu: "toolbarToggleSecondaryMenuHandler" },
            { content: "[View C]", classes: "content", style: "background-color: rgb(192,255,192);" } ]
        },
        { name: "d", classes: "view",
          components: [
            { kind: "Toolbar",
              onToggleMenu: "toolbarToggleMenuHandler",
              onToggleSecondaryMenu: "toolbarToggleSecondaryMenuHandler" },
            { content: "[View D]", classes: "content", style: "background-color: white" } ]
        },
        { name: "e", classes: "view",
          components: [
            { kind: "Toolbar",
              onToggleMenu: "toolbarToggleMenuHandler",
              onToggleSecondaryMenu: "toolbarToggleSecondaryMenuHandler" },
            { content: "[View E]", classes: "content", style: "color: white; background-color: black" } ]
        },
        { name: "f", classes: "view",
          components: [
            { kind: "Toolbar",
              onToggleMenu: "toolbarToggleMenuHandler",
              onToggleSecondaryMenu: "toolbarToggleSecondaryMenuHandler" },
            { content: "[View F]", classes: "content" }
          ]
        }
      ]
    }
  ],

  viewChangedHandler: function(inSender, inEvent) {
    this.log();
  },
  menuOpenedHandler: function(inSender, inEvent) {
    this.log();
  },
  menuClosedHandler: function(inSender, inEvent) {
    this.log();
  },
  secondaryMenuOpenedHandler: function(inSender, inEvent) {
    this.log();
  },
  secondaryMenuClosedHandler: function(inSender, inEvent) {
    this.log();
  },

  toolbarToggleMenuHandler: function(inSender, inEvent) {
    this.log();
    this.$.menupane.toggleMenu();
  },
  toolbarToggleSecondaryMenuHandler: function(inSender, inEvent) {
    this.log();
    this.$.menupane.toggleSecondaryMenu();
  }

});