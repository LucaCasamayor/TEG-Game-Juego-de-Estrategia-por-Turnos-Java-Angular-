import type {CountryConfig} from "../models/interfaces/map-config.interface"

export const TEG_COUNTRIES_CONFIG: CountryConfig[] = [
  // AMÉRICA DEL NORTE (Amarillo/Naranja)
  {
    id: "alaska",
    name: "Alaska",
    pathSelectors: ['path[fill*="orange"]', 'g[id*="alaska"] path', 'path[data-country="alaska"]'],
    continent: "america_norte",
    defaultColor: "#FFA500",
  },
  {
    id: "yukon",
    name: "Yukon",
    pathSelectors: ['g[id*="yukon"] path', 'path[data-country="yukon"]'],
    continent: "america_norte",
    defaultColor: "#FFB84D",
  },
  {
    id: "oregon",
    name: "Oregon",
    pathSelectors: ['g[id*="oregon"] path', 'path[data-country="oregon"]'],
    continent: "america_norte",
    defaultColor: "#FFC266",
  },
  {
    id: "california",
    name: "California",
    pathSelectors: ['g[id*="california"] path', 'path[data-country="california"]'],
    continent: "america_norte",
    defaultColor: "#FFCC80",
  },
  {
    id: "mexico",
    name: "México",
    pathSelectors: ['g[id*="mexico"] path', 'path[data-country="mexico"]'],
    continent: "america_norte",
    defaultColor: "#FFD699",
  },
  {
    id: "terranova",
    name: "Terranova",
    pathSelectors: ['g[id*="terranova"] path', 'path[data-country="terranova"]'],
    continent: "america_norte",
    defaultColor: "#FFE0B3",
  },
  {
    id: "nueva_york",
    name: "Nueva York",
    pathSelectors: ['g[id*="nueva_york"] path', 'path[data-country="nueva_york"]'],
    continent: "america_norte",
    defaultColor: "#FFEACC",
  },
  {
    id: "labrador",
    name: "Labrador",
    pathSelectors: ['g[id*="labrador"] path', 'path[data-country="labrador"]'],
    continent: "america_norte",
    defaultColor: "#FFF4E6",
  },
  {
    id: "canada",
    name: "Canadá",
    pathSelectors: ['g[id*="canada"] path', 'path[data-country="canada"]'],
    continent: "america_norte",
    defaultColor: "#FFCC99",
  },
  {
    id: "groenlandia",
    name: "Groenlandia",
    pathSelectors: ['g[id*="groenlandia"] path', 'path[data-country="groenlandia"]'],
    continent: "america_norte",
    defaultColor: "#FFB366",
  },

  // AMÉRICA DEL SUR (Verde claro)
  {
    id: "colombia",
    name: "Colombia",
    pathSelectors: ['g[id*="colombia"] path', 'path[data-country="colombia"]'],
    continent: "america_sur",
    defaultColor: "#90EE90",
  },
  {
    id: "peru",
    name: "Perú",
    pathSelectors: ['g[id*="peru"] path', 'path[data-country="peru"]'],
    continent: "america_sur",
    defaultColor: "#98FB98",
  },
  {
    id: "brasil",
    name: "Brasil",
    pathSelectors: ['g[id*="brasil"] path', 'path[data-country="brasil"]'],
    continent: "america_sur",
    defaultColor: "#90EE90",
  },
  {
    id: "chile",
    name: "Chile",
    pathSelectors: ['g[id*="chile"] path', 'path[data-country="chile"]'],
    continent: "america_sur",
    defaultColor: "#87CEEB",
  },
  {
    id: "argentina",
    name: "Argentina",
    pathSelectors: ['g[id*="argentina"] path', 'path[data-country="argentina"]'],
    continent: "america_sur",
    defaultColor: "#ADD8E6",
  },
  {
    id: "uruguay",
    name: "Uruguay",
    pathSelectors: ['g[id*="uruguay"] path', 'path[data-country="uruguay"]'],
    continent: "america_sur",
    defaultColor: "#B0E0E6",
  },

  // EUROPA (Rosa/Magenta)
  {
    id: "islandia",
    name: "Islandia",
    pathSelectors: ['g[id*="islandia"] path', 'path[data-country="islandia"]'],
    continent: "europa",
    defaultColor: "#FFB6C1",
  },
  {
    id: "gran_bretana",
    name: "Gran Bretaña",
    pathSelectors: ['g[id*="gran_bretana"] path', 'path[data-country="gran_bretana"]'],
    continent: "europa",
    defaultColor: "#FFC0CB",
  },
  {
    id: "francia",
    name: "Francia",
    pathSelectors: ['g[id*="francia"] path', 'path[data-country="francia"]'],
    continent: "europa",
    defaultColor: "#FFCCCB",
  },
  {
    id: "espana",
    name: "España",
    pathSelectors: ['g[id*="espana"] path', 'path[data-country="espana"]'],
    continent: "europa",
    defaultColor: "#FFD6CC",
  },
  {
    id: "italia",
    name: "Italia",
    pathSelectors: ['g[id*="italia"] path', 'path[data-country="italia"]'],
    continent: "europa",
    defaultColor: "#FFE0DD",
  },
  {
    id: "alemania",
    name: "Alemania",
    pathSelectors: ['g[id*="alemania"] path', 'path[data-country="alemania"]'],
    continent: "europa",
    defaultColor: "#FFEAEE",
  },
  {
    id: "polonia",
    name: "Polonia",
    pathSelectors: ['g[id*="polonia"] path', 'path[data-country="polonia"]'],
    continent: "europa",
    defaultColor: "#FFF4FF",
  },
  {
    id: "rusia",
    name: "Rusia",
    pathSelectors: ['g[id*="rusia"] path', 'path[data-country="rusia"]'],
    continent: "europa",
    defaultColor: "#E6E6FA",
  },
  {
    id: "suecia",
    name: "Suecia",
    pathSelectors: ['g[id*="suecia"] path', 'path[data-country="suecia"]'],
    continent: "europa",
    defaultColor: "#DDA0DD",
  },

  // ASIA (Verde oscuro) - CORREGIDO Y COMPLETO
  {
    id: "tartaria",
    name: "Tartaria",
    pathSelectors: ['g[id*="tartaria"] path', 'path[data-country="tartaria"]'],
    continent: "asia",
    defaultColor: "#228B22",
  },
  {
    id: "aral",
    name: "Aral",
    pathSelectors: ['g[id*="aral"] path', 'path[data-country="aral"]'],
    continent: "asia",
    defaultColor: "#2F4F4F",
  },
  {
    id: "tamir",
    name: "Tamir",
    pathSelectors: ['g[id*="tamir"] path', 'path[data-country="tamir"]'],
    continent: "asia",
    defaultColor: "#32CD32",
  },
  {
    id: "kamchatka",
    name: "Kamchatka",
    pathSelectors: ['g[id*="kamchatka"] path', 'path[data-country="kamchatka"]'],
    continent: "asia",
    defaultColor: "#3CB371",
  },
  {
    id: "siberia",
    name: "Siberia",
    pathSelectors: ['g[id*="siberia"] path', 'path[data-country="siberia"]'],
    continent: "asia",
    defaultColor: "#2E8B57",
  },
  {
    id: "mongolia",
    name: "Mongolia",
    pathSelectors: ['g[id*="mongolia"] path', 'path[data-country="mongolia"]'],
    continent: "asia",
    defaultColor: "#66CDAA",
  },
  {
    id: "gobi",
    name: "Gobi",
    pathSelectors: ['g[id*="gobi"] path', 'path[data-country="gobi"]'],
    continent: "asia",
    defaultColor: "#20B2AA",
  },
  {
    id: "china",
    name: "China",
    pathSelectors: ['g[id*="china"] path', 'path[data-country="china"]'],
    continent: "asia",
    defaultColor: "#48D1CC",
  },
  {
    id: "turquia",
    name: "Turquía",
    pathSelectors: ['g[id*="turquia"] path', 'path[data-country="turquia"]'],
    continent: "asia",
    defaultColor: "#40E0D0",
  },
  {
    id: "israel",
    name: "Israel",
    pathSelectors: ['g[id*="israel"] path', 'path[data-country="israel"]'],
    continent: "asia",
    defaultColor: "#AFEEEE",
  },
  {
    id: "arabia",
    name: "Arabia",
    pathSelectors: ['g[id*="arabia"] path', 'path[data-country="arabia"]'],
    continent: "asia",
    defaultColor: "#B0E0E6",
  },
  {
    id: "iran",
    name: "Irán",
    pathSelectors: ['g[id*="iran"] path', 'path[data-country="iran"]'],
    continent: "asia",
    defaultColor: "#87CEEB",
  },
  {
    id: "malasia",
    name: "Malasia",
    pathSelectors: ['g[id*="malasia"] path', 'path[data-country="malasia"]'],
    continent: "asia",
    defaultColor: "#E0FFFF",
  },
  {
    id: "india",
    name: "India",
    pathSelectors: ['g[id*="india"] path', 'path[data-country="india"]'],
    continent: "asia",
    defaultColor: "#F0FFFF",
  },

  // ÁFRICA (Rojo)
  {
    id: "sahara",
    name: "Sahara",
    pathSelectors: ['g[id*="sahara"] path', 'path[data-country="sahara"]'],
    continent: "africa",
    defaultColor: "#DC143C",
  },
  {
    id: "egipto",
    name: "Egipto",
    pathSelectors: ['g[id*="egipto"] path', 'path[data-country="egipto"]'],
    continent: "africa",
    defaultColor: "#B22222",
  },
  {
    id: "etiopia",
    name: "Etiopía",
    pathSelectors: ['g[id*="etiopia"] path', 'path[data-country="etiopia"]'],
    continent: "africa",
    defaultColor: "#CD5C5C",
  },
  {
    id: "zaire",
    name: "Zaire",
    pathSelectors: ['g[id*="zaire"] path', 'path[data-country="zaire"]'],
    continent: "africa",
    defaultColor: "#F08080",
  },
  {
    id: "sud_africa",
    name: "Sud África",
    pathSelectors: ['g[id*="sud_africa"] path', 'path[data-country="sud_africa"]'],
    continent: "africa",
    defaultColor: "#FA8072",
  },
  {
    id: "madagascar",
    name: "Madagascar",
    pathSelectors: ['g[id*="madagascar"] path', 'path[data-country="madagascar"]'],
    continent: "africa",
    defaultColor: "#E9967A",
  },

  // OCEANÍA (Azul)
  {
    id: "australia",
    name: "Australia",
    pathSelectors: ['g[id*="australia"] path', 'path[data-country="australia"]'],
    continent: "oceania",
    defaultColor: "#4169E1",
  },
  {
    id: "java",
    name: "Java",
    pathSelectors: ['g[id*="java"] path', 'path[data-country="java"]'],
    continent: "oceania",
    defaultColor: "#6495ED",
  },
  {
    id: "filipinas",
    name: "Filipinas",
    pathSelectors: ['g[id*="filipinas"] path', 'path[data-country="filipinas"]'],
    continent: "oceania",
    defaultColor: "#87CEEB",
  },
  {
    id: "sumatra",
    name: "Sumatra",
    pathSelectors: ['g[id*="sumatra"] path', 'path[data-country="sumatra"]'],
    continent: "oceania",
    defaultColor: "#B0C4DE",
  },
]

export const CONTINENT_COLORS = {
  america_norte: "#FFA500", // Naranja
  america_sur: "#90EE90", // Verde claro
  europa: "#FFB6C1", // Rosa
  asia: "#228B22", // Verde oscuro
  africa: "#DC143C", // Rojo
  oceania: "#4169E1", // Azul
}

// Configuración de continentes para bonificaciones
export const CONTINENT_BONUSES = {
  america_norte: 5,
  america_sur: 3,
  europa: 5,
  asia: 7,
  africa: 3,
  oceania: 2,
}

// Países fronterizos actualizados (incluyendo Aral e Irán)
export const COUNTRY_BORDERS: Record<string, string[]> = {
  // América del Norte
  alaska: ["yukon", "kamchatka"],
  yukon: ["alaska", "oregon", "terranova"],
  oregon: ["yukon", "california", "nueva_york"],
  california: ["oregon", "mexico", "nueva_york"],
  mexico: ["california", "colombia"],
  terranova: ["yukon", "nueva_york", "labrador", "groenlandia"],
  nueva_york: ["oregon", "california", "terranova", "labrador"],
  labrador: ["terranova", "nueva_york", "canada", "groenlandia"],
  canada: ["labrador", "groenlandia"],
  groenlandia: ["terranova", "labrador", "canada", "islandia"],

  // América del Sur
  colombia: ["mexico", "peru", "brasil"],
  peru: ["colombia", "brasil", "chile", "argentina"],
  brasil: ["colombia", "peru", "argentina", "uruguay"],
  chile: ["peru", "argentina"],
  argentina: ["peru", "brasil", "chile", "uruguay"],
  uruguay: ["brasil", "argentina"],

  // Europa
  islandia: ["groenlandia", "gran_bretana", "suecia"],
  gran_bretana: ["islandia", "francia", "alemania", "suecia"],
  francia: ["gran_bretana", "espana", "italia", "alemania"],
  espana: ["francia", "italia", "sahara"],
  italia: ["francia", "espana", "alemania", "polonia", "egipto", "turquia"],
  alemania: ["gran_bretana", "francia", "italia", "polonia", "suecia"],
  polonia: ["italia", "alemania", "suecia", "rusia", "turquia"],
  rusia: ["polonia", "suecia", "tartaria", "aral", "turquia"],
  suecia: ["islandia", "gran_bretana", "alemania", "polonia", "rusia"],

  // Asia (actualizado con Aral e Irán)
  tartaria: ["rusia", "aral", "tamir", "siberia", "mongolia"],
  aral: ["rusia", "tartaria", "iran", "turquia", "tamir"],
  tamir: ["tartaria", "aral", "kamchatka", "siberia", "mongolia", "china"],
  kamchatka: ["alaska", "tamir", "mongolia", "china"],
  siberia: ["tartaria", "tamir", "mongolia"],
  mongolia: ["tartaria", "tamir", "kamchatka", "siberia", "gobi", "china"],
  gobi: ["mongolia", "china"],
  china: ["tamir", "kamchatka", "mongolia", "gobi", "india", "malasia"],
  turquia: ["italia", "polonia", "rusia", "aral", "iran", "israel", "arabia"],
  iran: ["aral", "turquia", "israel", "arabia", "india"],
  israel: ["turquia", "iran", "arabia", "egipto"],
  arabia: ["turquia", "iran", "israel", "india", "egipto"],
  malasia: ["china", "india", "java", "sumatra"],
  india: ["china", "iran", "arabia", "malasia"],

  // África
  sahara: ["espana", "egipto", "etiopia", "zaire"],
  egipto: ["italia", "israel", "arabia", "sahara", "etiopia"],
  etiopia: ["sahara", "egipto", "zaire", "sud_africa", "madagascar"],
  zaire: ["sahara", "etiopia", "sud_africa"],
  sud_africa: ["etiopia", "zaire", "madagascar"],
  madagascar: ["etiopia", "sud_africa"],

  // Oceanía
  australia: ["java", "sumatra"],
  java: ["malasia", "australia", "sumatra", "filipinas"],
  filipinas: ["java", "sumatra"],
  sumatra: ["malasia", "australia", "java", "filipinas"],
}

// Conteo actualizado de países por continente
export const CONTINENT_COUNTRY_COUNT = {
  america_norte: 10,
  america_sur: 6,
  europa: 9,
  asia: 15, // Actualizado: ahora incluye Aral e Irán
  africa: 6,
  oceania: 4,
}
