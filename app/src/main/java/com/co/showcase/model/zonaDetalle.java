package com.co.showcase.model;

import com.co.showcase.model.geoJson.geoJson;
import java.util.List;

/**
 * Created by home on 13/08/16.
 */

public class zonaDetalle {

  /**
   * estado : exito
   * mensaje : Establecimientos obtenidos exitosamente
   * categorias : [{"id":1,"nombre":"Almacenes","establecimientos":[{"id":1,"nombre":"ESTABLECIMIENTO
   * 1","logo":"https://test.showcase.com.co/imagenes/logos/cc1b91f532c1b888d9056792a282fa8dc7e28f4f.png","marcador":1}]},{"id":2,"nombre":"Droguerias","establecimientos":[{"id":2,"nombre":"ESTABLECIMIENTO
   * 2","logo":"https://test.showcase.com.co/imagenes/logos/8eef95ac6a86abe3f5f32491a89220cca18680e2.png","marcador":2}]}]
   * totalPaginas : 0
   * pagina : 0
   * localizacion : {"type":"FeatureCollection","feature":[{"type":"Feature","propertie":{"marker-color":"#f5a623","marker-size":"small","marker-symbol":1},"geometry":{"type":"Point","coordinates":["2.4428106556474094","-76.61161422729492"]}},{"type":"Feature","propertie":{"marker-color":"#f5a623","marker-size":"small","marker-symbol":2},"geometry":{"type":"Point","coordinates":["2.437788398396185","-76.61178588867188"]}}]}
   */

  public String estado;
  public String mensaje;
  public int totalPaginas;
  public int pagina;

  /**
   * id : 1
   * nombre : Almacenes
   * establecimientos : [{"id":1,"nombre":"ESTABLECIMIENTO 1","logo":"https://test.showcase.com.co/imagenes/logos/cc1b91f532c1b888d9056792a282fa8dc7e28f4f.png","marcador":1}]
   */

  public List<Categoria> categorias;
  public geoJson localizacion;
}
