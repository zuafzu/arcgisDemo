package com.android.arcgisdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SketchCreationMode;
import com.esri.arcgisruntime.mapping.view.SketchEditor;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

public class MainActivity extends AppCompatActivity {

    private SimpleMarkerSymbol mPointSymbol;
    private SimpleLineSymbol mLineSymbol;
    private SimpleFillSymbol mFillSymbol;

    private SketchEditor mSketchEditor;

    private Button mPointButton;
    private Button mMultiPointButton;
    private Button mPolylineButton;
    private Button mPolygonButton;
    private Button mFreehandLineButton;
    private Button mFreehandPolygonButton;

    private Button undo;
    private Button redo;
    private Button stop;


    private MapView mMapView;
    private GraphicsOverlay mGraphicsOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = findViewById(R.id.mapView);

        mPointButton = findViewById(R.id.pointButton);
        mMultiPointButton = findViewById(R.id.pointsButton);
        mPolylineButton = findViewById(R.id.polylineButton);
        mPolygonButton = findViewById(R.id.polygonButton);
        mFreehandLineButton = findViewById(R.id.freehandLineButton);
        mFreehandPolygonButton = findViewById(R.id.freehandPolygonButton);

        undo = findViewById(R.id.undo);
        redo = findViewById(R.id.redo);
        stop = findViewById(R.id.stop);

        mPointButton.setOnClickListener(view -> createModePoint());
        mMultiPointButton.setOnClickListener(view -> createModeMultipoint());
        mPolylineButton.setOnClickListener(view -> createModePolyline());
        mPolygonButton.setOnClickListener(view -> createModePolygon());
        mFreehandLineButton.setOnClickListener(view -> createModeFreehandLine());
        mFreehandPolygonButton.setOnClickListener(view -> createModeFreehandPolygon());

        undo.setOnClickListener(view -> undo());
        redo.setOnClickListener(view -> redo());
        stop.setOnClickListener(view -> stop());

        // 创建一个新的草图编辑器并将其添加到地图视图中
        mSketchEditor = new SketchEditor();
        mMapView.setSketchEditor(mSketchEditor);
        mPointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, 0xFFFF0000, 20);
        mLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFFFF8800, 4);
        mFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.CROSS, 0x40FFA9A9, mLineSymbol);

        setupMap();

        createGraphics();
    }

    private void setupMap() {
        if (mMapView != null) {
            Basemap.Type basemapType = Basemap.Type.STREETS_VECTOR;
            double latitude = 34.09042;
            double longitude = -118.71511;
            int levelOfDetail = 11;
            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            mMapView.setMap(map);
            mMapView.setAttributionTextVisible(false);//去掉下方logo

        }
    }

    private void createGraphicsOverlay() {
        mGraphicsOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(mGraphicsOverlay);
    }

    private void createPointGraphics() {
        Point point = new Point(-118.69333917997633, 34.032793670122885, SpatialReferences.getWgs84());
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.rgb(226, 119, 40), 10.0f);
        pointSymbol.setOutline(new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f));
        Graphic pointGraphic = new Graphic(point, pointSymbol);
        mGraphicsOverlay.getGraphics().add(pointGraphic);
    }

    private void createPolylineGraphics() {
        PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());
        polylinePoints.add(new Point(-118.67999016098526, 34.035828839974684));
        polylinePoints.add(new Point(-118.65702911071331, 34.07649252525452));
        Polyline polyline = new Polyline(polylinePoints);
        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3.0f);
        Graphic polylineGraphic = new Graphic(polyline, polylineSymbol);
        mGraphicsOverlay.getGraphics().add(polylineGraphic);
    }

    private void createPolygonGraphics() {
        PointCollection polygonPoints = new PointCollection(SpatialReferences.getWgs84());
        polygonPoints.add(new Point(-118.70372100524446, 34.03519536420519));
        polygonPoints.add(new Point(-118.71766916267414, 34.03505116445459));
        polygonPoints.add(new Point(-118.71923322580597, 34.04919407570509));
        polygonPoints.add(new Point(-118.71631129436038, 34.04915962906471));
        polygonPoints.add(new Point(-118.71526020370266, 34.059921300916244));
        polygonPoints.add(new Point(-118.71153226844807, 34.06035488360282));
        polygonPoints.add(new Point(-118.70803735010169, 34.05014385296186));
        polygonPoints.add(new Point(-118.69877903513455, 34.045182336992816));
        polygonPoints.add(new Point(-118.6979656552508, 34.040267760924316));
        polygonPoints.add(new Point(-118.70259112469694, 34.038800278306674));
        polygonPoints.add(new Point(-118.70372100524446, 34.03519536420519));
        Polygon polygon = new Polygon(polygonPoints);
        SimpleFillSymbol polygonSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.argb(55, 226, 119, 40),
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f));
        Graphic polygonGraphic = new Graphic(polygon, polygonSymbol);
        mGraphicsOverlay.getGraphics().add(polygonGraphic);
    }

    private void createGraphics() {
        createGraphicsOverlay();
//        createPointGraphics();
//        createPolylineGraphics();
//        createPolygonGraphics();
    }

    /**
     * 单击point按钮时，重置其他按钮，显示选中的point按钮和开始点绘图模式。
     */
    private void createModePoint() {
        resetButtons();
        mPointButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.POINT);
    }

    /**
     * 单击multipoint按钮时，重置其他按钮，显示选中的multipoint按钮，然后开始多点绘图模式。
     */
    private void createModeMultipoint() {
        resetButtons();
        mMultiPointButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.MULTIPOINT);
    }

    /**
     * 单击折线按钮时，重置其他按钮，显示选中的折线按钮，然后开始折线图模式。
     */
    private void createModePolyline() {
        resetButtons();
        mPolylineButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.POLYLINE);
    }

    /**
     * 当单击多边形按钮时，重置其他按钮，显示选中的多边形按钮，并启动多边形绘图模式。
     */
    private void createModePolygon() {
        resetButtons();
        mPolygonButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.POLYGON);
    }

    /**
     * 单击FREEHAND_LINE按钮时，重置其他按钮，显示选定的FREEHAND_LINE按钮，和开始徒手画线模式。
     */
    private void createModeFreehandLine() {
        resetButtons();
        mFreehandLineButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.FREEHAND_LINE);
    }

    /**
     * 单击FREEHAND_POLYGON按钮时，重置其他按钮，显示选定的FREEHAND_POLYGON按钮，并启用徒手绘制多边形模式。
     */
    private void createModeFreehandPolygon() {
        resetButtons();
        mFreehandPolygonButton.setSelected(true);
        mSketchEditor.start(SketchCreationMode.FREEHAND_POLYGON);
    }

    /**
     * 当单击undo按钮时，撤消SketchEditor上的最后一个事件。
     */
    private void undo() {
        if (mSketchEditor.canUndo()) {
            mSketchEditor.undo();
        }
    }

    /**
     * 当单击redo按钮时，在SketchEditor上重做最后一个未完成的事件。
     */
    private void redo() {
        if (mSketchEditor.canRedo()) {
            mSketchEditor.redo();
        }
    }

    /**
     * 当单击停止按钮时，检查草图是否有效。如果是，从草图中得到几何图形，设置它将其添加到图形覆盖层。
     */
    private void stop() {
        if (!mSketchEditor.isSketchValid()) {
            reportNotValid(); //如果草图无效，则调用。向用户报告草图无效的原因。
            mSketchEditor.stop();
            resetButtons();
            return;
        }
        // 从草图编辑器获得几何图形
        Geometry sketchGeometry = mSketchEditor.getGeometry();
        mSketchEditor.stop();
        resetButtons();
        if (sketchGeometry != null) { //从草图编辑器创建一个图形
            Graphic graphic = new Graphic(sketchGeometry);
            // 根据几何类型分配符号
            if (graphic.getGeometry().getGeometryType() == GeometryType.POLYGON) {
                graphic.setSymbol(mFillSymbol);
            } else if (graphic.getGeometry().getGeometryType() == GeometryType.POLYLINE) {
                graphic.setSymbol(mLineSymbol);
            } else if (graphic.getGeometry().getGeometryType() == GeometryType.POINT || graphic.getGeometry().getGeometryType() == GeometryType.MULTIPOINT) {
                graphic.setSymbol(mPointSymbol);
            }
            // 将图形添加到图形覆盖层
            mGraphicsOverlay.getGraphics().add(graphic);
        }
    }

    /**
     * 如果草图无效，则调用。向用户报告草图无效的原因。
     */
    private void reportNotValid() {
        String validIf;
        if (mSketchEditor.getSketchCreationMode() == SketchCreationMode.POINT) {
            validIf = "Point only valid if it contains an x & y coordinate.";
        } else if (mSketchEditor.getSketchCreationMode() == SketchCreationMode.MULTIPOINT) {
            validIf = "Multipoint only valid if it contains at least one vertex.";
        } else if (mSketchEditor.getSketchCreationMode() == SketchCreationMode.POLYLINE || mSketchEditor.getSketchCreationMode() == SketchCreationMode.FREEHAND_LINE) {
            validIf = "Polyline only valid if it contains at least one part of 2 or more vertices.";
        } else if (mSketchEditor.getSketchCreationMode() == SketchCreationMode.POLYGON || mSketchEditor.getSketchCreationMode() == SketchCreationMode.FREEHAND_POLYGON) {
            validIf = "Polygon only valid if it contains at least one part of 3 or more vertices which form a closed ring.";
        } else {
            validIf = "No sketch creation mode selected.";
        }
        String report = "Sketch geometry invalid:\n" + validIf;

        Log.e("cyf", "错误 ： "+report);
    }

    /**
     * De-selects all buttons.
     */
    private void resetButtons() {
        mPointButton.setSelected(false);
        mMultiPointButton.setSelected(false);
        mPolylineButton.setSelected(false);
        mPolygonButton.setSelected(false);
        mFreehandLineButton.setSelected(false);
        mFreehandPolygonButton.setSelected(false);
    }

    @Override
    protected void onPause() {
        if (mMapView != null) {
            mMapView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mMapView != null) {
            mMapView.dispose();
        }
        super.onDestroy();
    }

}
