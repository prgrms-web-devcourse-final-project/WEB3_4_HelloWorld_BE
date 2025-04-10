package org.helloworld.gymmate.common.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {

    // meter 단위
    private static final double EARTH_RADIUS = 6371000.0;
    private static final double DEFAULT_RADIUS_METERS = 3000.0;

    public static Point createPoint(double longitude, double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    public static String toPolygonWKT(double lat, double lng) {
        return toPolygonWKT(lat, lng, DEFAULT_RADIUS_METERS);
    }

    public static String toPolygonWKT(double lat, double lng, double distanceMeters) {
        double deltaLat = Math.toDegrees(distanceMeters / EARTH_RADIUS);
        double deltaLng = Math.toDegrees(distanceMeters / (EARTH_RADIUS * Math.cos(Math.toRadians(lat))));

        double minLat = lat - deltaLat;
        double maxLat = lat + deltaLat;
        double minLng = lng - deltaLng;
        double maxLng = lng + deltaLng;

        return String.format(
            "POLYGON((%f %f, %f %f, %f %f, %f %f, %f %f))",
            minLng, minLat,
            maxLng, minLat,
            maxLng, maxLat,
            minLng, maxLat,
            minLng, minLat
        );
    }
}
